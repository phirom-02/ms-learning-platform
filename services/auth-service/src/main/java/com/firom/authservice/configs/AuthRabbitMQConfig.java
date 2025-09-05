package com.firom.authservice.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRabbitMQConfig {

    public final static String QUEUE_NAME = "auth-message-queue";
    public final static String QUEUE_EXCHANGE = "auth-message-exchange";
    public final static String ROUTING_KEY = "auth.notification";

    @Bean
    public Queue authQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(QUEUE_EXCHANGE);
    }

    @Bean
    public Binding authBinding() {
        return BindingBuilder.bind(authQueue()).to(authExchange()).with(ROUTING_KEY);
    }
}
