package com.firom.lms.configs.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnrollmentNoticeRabbitMQConfig {

    @Bean
    public Queue enrollmentQueue() {
        return new Queue("notification.enrollment_notice.queue");
    }

    @Bean
    public DirectExchange enrollmentExchange() {
        return new DirectExchange("enrollment.enrollment_notice.exchange");
    }

    @Bean
    public Binding enrollmentBinding() {
        return BindingBuilder
                .bind(enrollmentQueue())
                .to(enrollmentExchange())
                .with("enrollment.notice");
    }
}
