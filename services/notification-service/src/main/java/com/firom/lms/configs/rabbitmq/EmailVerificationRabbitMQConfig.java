package com.firom.lms.configs.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailVerificationRabbitMQConfig {

    @Bean
    public Queue authQueue() {
        return new Queue("notification.email_verification.queue");
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange("auth.email_verification.exchange");
    }

    @Bean
    public Binding authBinding() {
        return BindingBuilder
                .bind(authQueue())
                .to(authExchange())
                .with("email.verification");
    }
}
