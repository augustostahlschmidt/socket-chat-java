package base;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void executeServer() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandlerRunnable clientHandlerRunnable = new ClientHandlerRunnable(clientSocket);
            new Thread(clientHandlerRunnable).start();
        }
    }
}