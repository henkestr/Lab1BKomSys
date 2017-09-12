package Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private ArrayList<ClientHandler> clients;
    private Socket clientSocket;
    private String nickname;

    public ClientHandler(ArrayList<ClientHandler> clients, Socket clientSocket) {
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
                switch (msg){
                    case "/quit":
                        // Skapa metod för denna
                        clientSocket.close();
                        break;
                    case "/who":
                        //Metod
                        break;
                    case "/nick":
                        //Metod
                        break;
                    case "/help":
                        //Metod
                        break;
                }

                System.out.println(nickname);
                for (ClientHandler ch : clients) {
                    if (ch.clientSocket != clientSocket) {
                        printWriter = new PrintWriter(ch.clientSocket.getOutputStream(), true);
                        printWriter.println(String.format("%s says: %s", nickname, msg));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String commandWho(){
        StringBuilder sb = new StringBuilder();
        return null;
    }
}
