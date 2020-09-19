package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Client {
    private String uuid;
    private String username;
    private Socket serverSocket;

    private PrintWriter out;
    private BufferedReader in;

    public Client(String username, String serverIp, int serverPort) throws IOException{
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.serverSocket = new Socket(serverIp, serverPort);
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.out = new PrintWriter(serverSocket.getOutputStream(), true);
    }

    public void sendToServer(String message){
        String[] parts = message.split(" ");
        String command = parts[0];
        String text = "";
        if(parts.length > 1)
            for(int i = 1; i< parts.length; i++)
                if(i != parts.length - 1)
                    text += parts[i] + " ";
                else
                    text += parts[i];

        String completeMessage = this.username + "_" + command + "_" + text;
        out.println(completeMessage);
    }

    public ArrayList<String> getMessages() throws IOException {
        ArrayList<String> formattedMessages = new ArrayList<>();
        String formattedMessage = "";

        this.sendToServer("get");
        String completeMessage = in.readLine();

        String parts[] = completeMessage.split("#");
        for(String part : parts){
            String line[] = part.split("_");
            String username = line[0];
            String message = line[2];

            formattedMessage = username + ": " + message;
            formattedMessages.add(formattedMessage);
        }
        return formattedMessages;
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }
}