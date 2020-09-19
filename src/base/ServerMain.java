package base;

import java.io.IOException;

import netcode.Server;

public class ServerMain {
	public static void main(String[] args) throws IOException {		
		Server server = new Server();
		server.createServer(9092);
		System.out.println("[SERVER] Server running...");		
		server.executeServer();	
	}
}
