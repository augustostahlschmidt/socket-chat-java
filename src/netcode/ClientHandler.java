package netcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
	private Socket clientSocket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String clientMessage = null;
	
	public ClientHandler(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				this.clientMessage = in.readLine();		
				
				System.out.println("[SERVER] Client sent: \"" + clientMessage + "\""); 
				out.println("RESPONDING TO YOUR \"" + clientMessage + "\"");
				System.out.println("[SERVER] Response sent to client.");
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.close();			
		}
	}	
}
