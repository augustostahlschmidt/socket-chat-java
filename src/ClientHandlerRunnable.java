import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientHandlerRunnable implements Runnable {
    private List<ClientHandlerRunnable> clients;
    private Socket clientSocket;
    public PrintWriter out;
    private BufferedReader in;
    private String clientMessage;
    private String username;
    private List<ChatRoom> rooms;
    private Boolean connected;
    private ChatRoom connectedRoom;

    public ClientHandlerRunnable(Socket clientSocket, List<ClientHandlerRunnable> clients, List<ChatRoom> rooms) throws IOException{
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.rooms = rooms;
        this.connected = false;
        this.connectedRoom = null;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String clientMessage = in.readLine();

                if(!connected){
                    if (clientMessage.contains("create")){
                        String chatRoomKey = createChatRoom(this);
                        this.out.println("[CREATED CHAT ROOM SECRET KEY] " + chatRoomKey);
                        this.connected = true;
                    }
                    else if (clientMessage.contains("connect")){
                        if(connectToRoom(clientMessage, this)){
                            this.out.println("[CONNECTED]");
                            this.connected = true;
                        } else {
                            this.out.println("Could not connect, room not found.");
                        }
                    }
                    else{
                        this.out.println("[INVALID COMMAND]");
                    }
                }
                else{
                    if (clientMessage.contains("_quit")){
                        removeClientFromRoom();
                    }
                    else
                        this.connectedRoom.sendMessage(clientMessage, this);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean connectToRoom(String clientMessage, ClientHandlerRunnable client) {
        String parts[] = clientMessage.split(" ");
        String chatRoomKey = parts[1];
        for(ChatRoom room : rooms){
            if(room.getKey().equals(chatRoomKey)){
                room.addClient(client);
                this.connectedRoom = room;                
                return true;
            }
        }
        return false;
    }

    private void removeClientFromRoom() {
        connectedRoom.removeClient(this);
    }

    private String createChatRoom(ClientHandlerRunnable client) {
        ChatRoom room = new ChatRoom();
        this.connectedRoom = room;
        room.addClient(client);
        rooms.add(room);
        return room.getKey();
    }

    public void close() throws IOException {
        this.clientSocket.close();
    }
}