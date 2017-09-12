package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 5000;


    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<Socket> clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress());

                clients.add(socket);
                // Starting new thread
                Thread clientHandler = new Thread(new ClientHandler(clients, socket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
