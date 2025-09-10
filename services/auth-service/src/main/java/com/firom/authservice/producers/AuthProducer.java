package com.firom.authservice.producers;

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
                "auth.email_verification.exchange",
                "email.verification",
                message
        );
    }
}

