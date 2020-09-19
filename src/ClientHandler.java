package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class ClientHandler {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientMessage;

    public ClientHandler(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void sendToClient(String message){
        out.println(message);
    }

    public void receiveFromClient() throws IOException {
        this.clientMessage = in.readLine();
    }

    public String getReceivedMessage(){
        return this.clientMessage;
    }

    public void close() throws IOException {
        this.clientSocket.close();
    }
}