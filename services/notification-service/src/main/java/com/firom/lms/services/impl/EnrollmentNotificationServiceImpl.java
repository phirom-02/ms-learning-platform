package com.firom.lms.services.impl;

import com.firom.lms.constants.EmailTemplates;
import com.firom.lms.services.EnrollmentNotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollmentNotificationServiceImpl implements EnrollmentNotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    @Override
    public void sendEnrollmentNotice(
            String courseTitle,
            String studentEmail,
            String studentUsername,
            String instructorUsername
    ) {
        // Set up mail sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        // Set variables to Thymeleaf context in-order to replace template's placeholders
        Map<String, Object> variables = new HashMap<>();
        variables.put("studentUsername", studentUsername);
        variables.put("courseTitle", courseTitle);
        variables.put("instructorUsername", instructorUsername);
        Context context = new Context();
        context.setVariables(variables);

        try {
            // Set up html template
            String templateName = EmailTemplates.COURSE_ENROLLMENT_NOTICE.getTemplate();
            String htmlTemplate = templateEngine.process(templateName, context);

            // Set up message
            messageHelper.setSubject(EmailTemplates.COURSE_ENROLLMENT_NOTICE.getSubject());
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(studentEmail);

            // Send
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", studentEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send email verification to {}", studentEmail);
        }
    }
}
