package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<ClientHandler> clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(String.format("A user with IP %s connected to the server",socket.getInetAddress()));

                ClientHandler clientHandler = new ClientHandler(clients, socket);
                clients.add(clientHandler);
                // Starting new thread
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
