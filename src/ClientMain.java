package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientMain {
    public static void main(String[] args) throws IOException{
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String keyboardInput = "";
        System.out.print("[CLIENT] Type your username: ");
        String username = keyboard.readLine();

        Client client = new Client(username, "127.0.0.1", 9091);
        System.out.println("[CLIENT] Chat Terminal opened for " + username + ".");


        ServerHandlerRunnable serverHandlerRunnable = new ServerHandlerRunnable(client);
        new Thread(serverHandlerRunnable).start();


        while(keyboardInput != "!q") {
            keyboardInput = keyboard.readLine();

            if(keyboardInput.contains("send")){
                client.sendToServer(keyboardInput);
            }
        }
        client.close();
    }
}