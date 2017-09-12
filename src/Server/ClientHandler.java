package Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private ArrayList<Socket> clients;
    private Socket clientSocket;
    private String nickname;

    public ClientHandler(ArrayList<Socket> clients, Socket clientSocket) {
        this.clients = clients;
        this.clientSocket = clientSocket;
        nickname = "User" + clientSocket.getPort();
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printWriter = null;

            while (true) {
                String msg = bufferedReader.readLine();
                System.out.println(nickname);
                for (Socket s : clients) {
                    if (s != clientSocket) {
                        printWriter = new PrintWriter(s.getOutputStream(), true);
                        printWriter.println(String.format("%s says: %s", nickname, msg));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
