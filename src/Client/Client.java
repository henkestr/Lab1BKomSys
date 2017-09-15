package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader buffReader = null;
        try {
            // Connecting to the server
            InetAddress serverAdress = InetAddress.getLocalHost();
            socket = new Socket(serverAdress, PORT);

            // Starting new thread
            Thread stringSender = new Thread(new StringSender(socket));
            stringSender.start();
            // Read
            buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String msg = buffReader.readLine();
                if (msg.equals("byebye")){
                    continue;
                }else
                    System.out.println(msg);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Lost connection to the server");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
