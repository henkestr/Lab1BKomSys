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
                    default:
                        if(msg.indexOf("/") == 0) {
                            sendMessage("Incorrect command", clientSocket);
                            continue;
                        }
                        break;
                }

                System.out.println(String.format("%s wrote a message",nickname));
                for (ClientHandler ch : clients) {
                    if (ch.clientSocket != clientSocket) {
                        printWriter = new PrintWriter(ch.clientSocket.getOutputStream(), true);
                        printWriter.println(String.format("%s says: %s", nickname, msg));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(String.format("Socket to user %s is closed", this.nickname));
        }
        finally {
            if(clientSocket != null) {
                //clients.remove(this);
                removeClient(clients, this);
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendMessage(String msg, Socket client){
        for (int i = 0; i<10; i++){
            try {
                PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
                printWriter.println(msg);
                break;
            } catch (IOException e) {
                System.out.println("Could not send message. Retrying to send");
            }
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
        //clients.remove(this);
        removeClient(clients, this);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(clientSocket != null)
                clientSocket = null;
        }
    }

    private synchronized void removeClient(ArrayList<ClientHandler> clientHandlers, ClientHandler client) {
        clientHandlers.remove(client);
    }
}
