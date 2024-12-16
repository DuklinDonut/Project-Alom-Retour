package listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RetourMessageListener {

    @KafkaListener(topics = {"dm-topic", "channel-topic"}, groupId = "retour-service-group")
    public void listen(String message) {
        System.out.println("New message received: " + message);
        // Ici, vous devez transmettre le message aux utilisateurs via sockets
        // Exemple : envoyer aux connexions actives
    }
}
