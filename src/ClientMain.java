package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientMain {
    public static void main(String[] args) throws IOException{
        Client client = new Client("Augusto", "127.0.0.1", 9091);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        String keyboardInput = "";
        while(keyboardInput != "!q") {
            System.out.print("> ");
            keyboardInput = keyboard.readLine();

            if(keyboardInput.contains("send")){
                client.sendToServer(keyboardInput);
            }else if(keyboardInput.contains("get")){
                ArrayList<String> messages = client.getMessages();
                for(String message : messages){
                    System.out.println(message);
                }
            }

        }
        client.close();
    }
}