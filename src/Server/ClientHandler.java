package Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
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
                switch (msg.split(" ")[0]){
                    case "/quit":
                        commandQuit();
                        break;
                    case "/who":
                        sendMessage(commandWho(),clientSocket);
                        continue;
                    case  "/nick":
                        commandNickname(msg.split(" ")[1]);
                        continue;
                    case "/help":
                        sendMessage(commandHelp(),clientSocket);
                        continue;
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
            System.out.println("Socket to user"+ this.nickname + " is closed");
        }
    }
    private void sendMessage(String msg, Socket client){
        try {
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            printWriter.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String commandWho(){
        StringBuilder sb = new StringBuilder();
        for (ClientHandler ch:clients) {
            sb.append(ch.nickname + "\n");
        }
        return sb.toString();
    }

    private void commandNickname(String nickname){
        this.nickname = nickname;
    }

    private String commandHelp(){
        return "Available commands: \n /who \n /nick \n /quit ";
    }

    private void commandQuit() {
        sendMessage("byebye", clientSocket);
        clients.remove(this);
        try {
            clientSocket.close();
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
