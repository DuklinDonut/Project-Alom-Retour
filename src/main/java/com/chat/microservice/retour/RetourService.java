package com.chat.microservice.retour;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RetourService {
    private RestTemplate restTemplate = new RestTemplate();

    public boolean validateToken(String token) {
        String url = "http://localhost:8081/authentication-service/api/validateToken";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, token, Boolean.class);
        return response.getBody();
    }

    public void handleConnection(String token) {
        if (validateToken(token)) {
            System.out.println("Token valid. Connection accepted.");
            // Ajouter la connexion socket logic ici
        } else {
            System.out.println("Invalid token. Connection refused.");
        }
    }
}