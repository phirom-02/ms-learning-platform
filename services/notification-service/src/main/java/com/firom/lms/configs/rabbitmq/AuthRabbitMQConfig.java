package com.firom.lms.configs.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ(s) configuration for Auth queue
 */
@Configuration
public class AuthRabbitMQConfig {

    public final static String EMAIL_VERIFICATION_QUEUE = "email-verification";
    public final static String EMAIL_VERIFICATION_EXCHANGE = "email-verification-exchange";
    public final static String ROUTING_KEY = "auth.notification";

    @Bean
    public Queue authQueue() {
        return new Queue(EMAIL_VERIFICATION_QUEUE, false);
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(EMAIL_VERIFICATION_EXCHANGE);
    }

    @Bean
    public Binding authBinding() {
        return BindingBuilder.bind(authQueue())
                .to(authExchange())
                .with(ROUTING_KEY);
    }
}
