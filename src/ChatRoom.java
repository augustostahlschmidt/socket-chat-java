package base;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String key;
    private List<ClientHandlerRunnable> clients;

    public ChatRoom(){
        generateKey();
        this.clients = new ArrayList<>();
    }

    private void generateKey() {
        RandomString randomString = new RandomString(2);
        this.key = randomString.nextString();
    }

    public String getKey(){
        return this.key;
    }

    public void addClient(ClientHandlerRunnable client){
        clients.add(client);
    }

    public void sendMessage(String clientMessage, ClientHandlerRunnable sender) {
        System.out.println("[SERVER] Message \"" + clientMessage + "\"" + " received.");

        for (ClientHandlerRunnable client : clients) {
            if (client != sender)
                client.out.println(clientMessage);
        }
    }
}
