package com.chat.microservice.retour;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RetourServer {
    public static void main(String[] args) {
        SpringApplication.run(RetourServer.class, args);

        // Démarrage du serveur TCP
        TCPServer tcpServer = new TCPServer();
        new Thread(tcpServer).start();
    }
}
