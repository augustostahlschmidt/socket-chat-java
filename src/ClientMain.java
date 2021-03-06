import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class ClientMain {
    public static Client client;

    public static void main(String[] args) throws IOException{
        String username;
        String command;
        String serverResponse;
        int tryCount = 0;

        print("Type your username: ");
        username = readKeyboard();
        client = createClient(username);

        while(tryCount < 3){
            println("Type [1] to create a chat room.\n" +
                    "Type [2] to connect to a chat room.");
            pollCommandAndProcess();
            tryCount++;
        }
        println("Exceeded tries. [QUITTING]");
        exit(-1);
    }

    private static Boolean pollCommandAndProcess() throws IOException {
        String command = readKeyboard();
        String serverResponse = sendCommandToServer(command);
        return processServerResponse(serverResponse);
    }

    private static Boolean processServerResponse(String serverResponse) {
        if(serverResponse.contains("[CREATED CHAT ROOM SECRET KEY]") ) {
            try {
                client.createCipher();
                print("[CREATED CLIENT SECRET KEY] " + client.getSecretKey() + "\n");
                startChatMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else if (serverResponse.contains("[CONNECTED]")){
            try {
                print("Enter the [CLIENT SECRET KEY] ");
                String inputSecretKey = readKeyboard();
                client.createCipher(inputSecretKey);
                startChatMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void startChatMode() throws IOException {
        ServerHandlerRunnable serverHandlerRunnable = new ServerHandlerRunnable(client);
        new Thread(serverHandlerRunnable).start();

        String chatMessage = "";
        println("[CHAT STARTED] Type _quit to quit.");
        println("-------------------------------------------------------");
        while(!(chatMessage.equals("_quit"))){
            try {
                chatMessage = readKeyboard();
                client.sendChatMessageToServer(chatMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        client.sendQuitSignal();
        client.close();
        println("Quitting...");
        exit(0);
    }

    public static String sendCommandToServer(String command) throws IOException {
        String serverResponse = "";

        if(command.equals("1")){
            client.sendToServer("create");
            serverResponse = getServerResponse();
        } else if(command.equals("2")){
            print("Enter the [CHAT ROOM SECRET KEY] ");
            String chatRoomKey = readKeyboard();
            client.sendToServer("connect " + chatRoomKey);
            serverResponse = getServerResponse();
        }

        println(serverResponse);
        return serverResponse;
    }

    private static String getServerResponse() {
        try {
            return client.in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String readKeyboard() throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        return keyboard.readLine();
    }

    public static void println(String message){
        System.out.println(message);
    }

    public static void print(String message){
        System.out.print(message);
    }

    public static Client createClient(String username) throws IOException {
        return new Client(username,"127.0.0.1", 9091);
    }
}