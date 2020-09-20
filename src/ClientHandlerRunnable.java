package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientHandlerRunnable implements Runnable {
    private List<ClientHandlerRunnable> clients;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientMessage;
    private String username;

    public ClientHandlerRunnable(Socket clientSocket, List<ClientHandlerRunnable> clients) throws IOException{
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = "";
                receivedMessage = in.readLine();

                if (receivedMessage.contains("send")) {
                    System.out.println("[SERVER] Message \"" + receivedMessage + "\"" + " received.");
                    this.username = extractName(receivedMessage);

                    for (ClientHandlerRunnable client : clients) {
                        if (client != this)
                            client.out.println(receivedMessage);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String extractName(String message){
        String parts[] = message.split("_");
        return parts[0];
    }

    public void close() throws IOException {
        this.clientSocket.close();
    }
}