package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
	public static void main(String[] args) throws IOException{		
		Client client = new Client("Augusto", "127.0.0.1", 9090);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("[CLIENT] Running client terminal... Type \"!q\" to quit.");
		String keyboardInput = "";
		while(keyboardInput != "!q") {

			System.out.print("> ");
			keyboardInput = keyboard.readLine();
			
			client.sendToServer(keyboardInput);		
			System.out.println("[CLIENT] Sent \"" + keyboardInput + "\" to server.");
			
			String serverMessage = client.receiveFromServer();
			System.out.println("[CLIENT] Server has responded: \"" + serverMessage + "\"");
		}
		
		System.out.println("[CLIENT] Closing all connections...");
		client.close();

	}
}
