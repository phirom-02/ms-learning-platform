package com.firom.lms.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthNotificationProducer {
    @Value("${application.rabbitmq.auth.queue-exchange}")
    public static String queueExchange;
    @Value("${application.rabbitmq.auth.routing-key}")
    public static String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public AuthNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuthNotification(String message) {
        rabbitTemplate.convertAndSend(
                queueExchange,
                routingKey,
                message
        );
    }
}
