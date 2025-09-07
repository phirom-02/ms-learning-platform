package com.firom.lms.consumers;

import com.firom.lms.configs.rabbitmq.AuthRabbitMQConfig;
import com.firom.lms.services.AuthNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthConsumer {

    private final AuthNotificationService authNotificationService;

    @RabbitListener(queues = AuthRabbitMQConfig.EMAIL_VERIFICATION_QUEUE)
    public void consumeEmailVerification(EmailVerificationMessage message) {
        log.info("Received email verification message: {}", message);
        authNotificationService.sendEmailVerification(
                message.getEmail(),
                message.getUsername(),
                message.getVerificationToken()
        );
    }
}
