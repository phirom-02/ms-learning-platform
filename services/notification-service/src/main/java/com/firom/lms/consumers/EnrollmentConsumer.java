package com.firom.lms.consumers;

import com.firom.lms.services.EnrollmentNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentConsumer {

    private final EnrollmentNotificationService enrollmentNotificationService;

    @RabbitListener(queues = "notification.enrollment_notice.queue")
    public void consumeEmailVerification(EnrollmentMessage message) {
        log.info("Received enrollment notice message: {}", message);
        enrollmentNotificationService.sendEnrollmentNotice(
                message.getCourseTitle(),
                message.getStudentEmail(),
                message.getStudentUsername(),
                message.getInstructorUsername()
        );
    }
}
