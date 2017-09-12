package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class StringSender implements Runnable {
    private Socket serverSocket;

    public StringSender(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            PrintWriter writer = new PrintWriter(serverSocket.getOutputStream(), true);
            while (true)
                writer.println(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
