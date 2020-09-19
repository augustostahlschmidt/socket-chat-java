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
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientMessage;

    public ClientHandlerRunnable(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        List<String> messages = new ArrayList<>();
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            String receivedMessage = "";
            try {
                receivedMessage = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (receivedMessage.contains("send")) {
                System.out.println("[SERVER] Message \"" + receivedMessage + "\"" + " received.");
                messages.add(receivedMessage);
            } else if (receivedMessage.contains("get")) {
                String allMessages = "";

                for (int i = 0; i<messages.size(); i++)
                    if(i != messages.size() - 1)
                        allMessages = allMessages + messages.get(i) + "#";
                    else
                        allMessages = allMessages + messages.get(i);

                out.println(allMessages);
            }
        }
    }

    public void close() throws IOException {
        this.clientSocket.close();
    }
}