package com.firom.lms.consumers;

import com.firom.lms.configs.rabbitmq.AuthRabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthMessageConsumer {

    @RabbitListener(queues = AuthRabbitMQConfig.QUEUE_NAME)
    public void consumeAuthNotification(String message) {
        System.out.println("Consumed Auth Notification: " + message);
    }
}
