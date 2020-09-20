package base;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandlerRunnable> clients;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();
    }

    public void executeServer() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandlerRunnable clientHandlerRunnable = new ClientHandlerRunnable(clientSocket, clients);
            clients.add(clientHandlerRunnable);
            new Thread(clientHandlerRunnable).start();
        }
    }
}