package com.firom.lms.producers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEnrollmentNotice(EnrollmentMessage message) {
        log.info("Sending enrollment notice message: {}", message);
        rabbitTemplate.convertAndSend(
                "enrollment.enrollment_notice.exchange",
                "enrollment.notice",
                message
        );
    }
}
