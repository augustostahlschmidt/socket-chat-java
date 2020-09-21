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
    public BufferedReader in;

    public Client(String username, String serverIp, int serverPort) throws IOException{
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.serverSocket = new Socket(serverIp, serverPort);
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.out = new PrintWriter(serverSocket.getOutputStream(), true);
    }

    public void sendToServer(String message){
        out.println(message);
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }

    public Socket getServerSocket() {
        return this.serverSocket;
    }

    public boolean hasConnectedToServer(){
        return this.serverSocket.isConnected();
    }

    public void setUsername(String username){
        this.username = username;
    }
}