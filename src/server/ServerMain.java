package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerMain {
    private static final int PORT = 12345;
    public static final List<ClientHandler> clientiConectati = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) {
        ObjectManager objectManager = new ObjectManager();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server pornit pe portul " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client conectat: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, objectManager);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Eroare la pornirea serverului: " + e.getMessage());
        }
    }
}