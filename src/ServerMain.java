import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server(9091);
        System.out.println("[SERVER] Server running...");
        server.executeServer();
    }
}