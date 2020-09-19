package netcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket serverSocket;	
	PrintWriter out;
	BufferedReader in;
	
	public Client() {
		this.serverSocket = null;
		this.in = null;
		this.out = null;
	}

	public void connectToServer(String ip, int port) throws UnknownHostException, IOException {
		serverSocket = new Socket(ip, port);
	}

	public void sendToServer(String input) throws IOException {
		if(out == null)
			out = new PrintWriter(serverSocket.getOutputStream(), true);
		out.println(input);
	}

	public String receiveFromServer() throws IOException {
		if(in == null)
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		return in.readLine();
	}

	public void close() throws IOException {
		if(serverSocket == null)
			return;
		this.serverSocket.close();
	}
}
