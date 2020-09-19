package base;

import base.ClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private ExecutorService clientHandlerExecutorService;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientHandlers = new ArrayList<>();
        this.clientHandlerExecutorService = Executors.newFixedThreadPool(16);
    }

    public void executeServer() throws IOException {
        List<ClientHandler> clients = new ArrayList<>();
        Thread thread;

        while(true) {
            Socket clientSocket = serverSocket.accept();

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);

            thread = new Thread(){
                @Override
                public void run() {
                    try{
                        clientHandler.receiveFromClient();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

            try{
                thread.join();

                for(ClientHandler client : clients){
                   client.sendToClient(clientHandler.getReceivedMessage());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}