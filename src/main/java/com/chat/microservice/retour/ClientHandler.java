package com.chat.microservice.retour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Map<String, String> authTokens; // Liste des tokens autorisés
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private String nickname;

    public ClientHandler(Socket clientSocket, Map<String, String> authTokens) {
        this.clientSocket = clientSocket;
        this.authTokens = authTokens;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Envoyer un message d'accueil
            out.println("Welcome to the chat server!");
            out.println("Please enter your authentication token:");

            // Lecture du token envoyé par le client
            String token = in.readLine();

            // Vérification du token
            if (authTokens.containsKey(token)) {
                nickname = authTokens.get(token);
                out.println("Authentication successful! Welcome, " + nickname + ".");
                System.out.println("User connected: " + nickname);

                // Démarrer un thread pour envoyer les messages
                new Thread(() -> {
                    try {
                        while (true) {
                            String message = messageQueue.take(); // Récupérer un message
                            out.println(message); // Envoyer au client
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();

                // Lecture des messages envoyés par le client
                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println(nickname + " says: " + input);
                    out.println("Echo: " + input); // Écho pour confirmation
                }

            } else {
                out.println("Invalid token. Connection closed.");
                clientSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Connection error with client: " + e.getMessage());
        }
    }

    // Ajouter un message à la file d'attente pour cet utilisateur
    public void sendMessage(String message) {
        messageQueue.add(message);
    }
}
