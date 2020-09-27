import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerHandlerRunnable implements Runnable {
    private Socket serverSocket;
    private BufferedReader in;
    private Client client;

    public ServerHandlerRunnable(Client client) throws IOException {
        this.serverSocket = client.getServerSocket();
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while(true) {
                String serverResponse = in.readLine();
                if (serverResponse == null) break;

                if(serverResponse.contains(":")){ // It's a chat message and needs to be decrypted!!
                    String decryptedMessage = client.decryptMessage(serverResponse);
                    if(decryptedMessage != null)
                        System.out.println(decryptedMessage);
                } else {
                    System.out.println(serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
