import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class Client {
    private String uuid;
    private String username;
    private Socket serverSocket;
    private PrintWriter out;
    public BufferedReader in;
    private ChatCipher chatCipher;

    public Client(String username, String serverIp, int serverPort) throws IOException{
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.serverSocket = new Socket(serverIp, serverPort);
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.out = new PrintWriter(serverSocket.getOutputStream(), true);
        this.chatCipher = null;
    }

    public void sendToServer(String message){
        out.println(message);
    }

    public void sendChatMessageToServer(String message){
        String encryptedMessage = this.encryptMessage(message);
        out.println(this.username + ": " + encryptedMessage);
    }

    public void sendQuitSignal(){
        out.println("_quit");
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }

    public Socket getServerSocket() {
        return this.serverSocket;
    }

    public boolean hasConnectedToServer(){
        return this.serverSocket.isConnected();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void createCipher(){
        this.chatCipher = new ChatCipher();
    }

    public void createCipher(String base64EncodedSecretKey){
        this.chatCipher = new ChatCipher(base64EncodedSecretKey);
    }

    public String getSecretKey(){
        return this.chatCipher.getBase64EncodedSecretKey();
    }

    public String encryptMessage(String message){
        return this.chatCipher.encrypt(message);
    }

    public String decryptMessage(String encryptedMessage){
        String parts[] = encryptedMessage.split(":");
        String trimmedMessage = parts[1].trim();
        String decryptedMessage = this.chatCipher.decrypt(trimmedMessage);
        if(decryptedMessage == null)
            return null;

        return parts[0] + ": " + decryptedMessage;
    }
}