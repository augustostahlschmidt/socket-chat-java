package base;

import base.ClientHandler;

import java.io.IOException;
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
		while(true) {
			Socket clientSocket = serverSocket.accept();
			ClientHandler clientHandlerThread = new ClientHandler(clientSocket);
			this.clientHandlerExecutorService.execute(clientHandlerThread);
		}
	}		
}
