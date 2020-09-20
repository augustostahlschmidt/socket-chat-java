package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerHandlerRunnable implements Runnable {
    private Socket serverSocket;
    private BufferedReader in;

    public ServerHandlerRunnable(Client client) throws IOException {
        this.serverSocket = client.getServerSocket();
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String serverResponse = in.readLine();

                String message = this.format(serverResponse);

                if (serverResponse == null) break;
                System.out.println(message);
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

    private String format(String serverResponse){
        String parts[] = serverResponse.split("_");

        String formattedResponse = parts[0] + ":";

        for(int i =2; i<parts.length; i++){
            formattedResponse += " " + parts[i];
        }

        return formattedResponse;
    }
}
