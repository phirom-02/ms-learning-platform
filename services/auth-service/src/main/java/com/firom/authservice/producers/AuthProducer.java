package com.firom.authservice.producers;

import com.firom.authservice.configs.AuthRabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmailVerification(EmailVerificationMessage message) {
        log.info("Sending email verification message: {}", message);
        rabbitTemplate.convertAndSend(
                AuthRabbitMQConfig.EMAIL_VERIFICATION_EXCHANGE,
                AuthRabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}

