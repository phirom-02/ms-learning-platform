package com.firom.authservice.producers;

import com.firom.authservice.configs.AuthRabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public AuthMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuthNotification(String message) {
        rabbitTemplate.convertAndSend(
                AuthRabbitMQConfig.QUEUE_EXCHANGE,
                AuthRabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}

