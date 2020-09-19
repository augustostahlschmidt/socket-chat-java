package netcode;

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
	
	public Server() {		
		this.serverSocket = null;	
		this.clientHandlers = new ArrayList<>();
		this.clientHandlerExecutorService = Executors.newFixedThreadPool(4);
	}
	
	public void createServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);		
	}
	
	public void executeServer() throws IOException {	
		while(true) {
			Socket clientSocket = serverSocket.accept();		

			ClientHandler clientHandlerThread = new ClientHandler(clientSocket);
			this.clientHandlers.add(clientHandlerThread);			
			this.clientHandlerExecutorService.execute(clientHandlerThread);
		}
	}		
}
