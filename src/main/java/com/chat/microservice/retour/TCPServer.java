package com.chat.microservice.retour;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPServer implements Runnable {

    private static final int PORT = 5000;
    private final Map<String, String> authTokens = new HashMap<>(); // Token-Nickname mapping

    // Initialisation avec des tokens simulés - Normalement fourni par Authentificate
    public TCPServer() {
        authTokens.put("token123", "user1");
        authTokens.put("token456", "user2");
    }
    //création utilisateur
   // kafka pour que autheticate puisse envoyer les token à retour
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP Server running on port " + PORT);

            while (true) {
                // Accepter une connexion client
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");
                
                // Créer un thread pour chaque client
                new Thread(new ClientHandler(clientSocket, authTokens)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
