package com.firom.lms.services.impl;

import com.firom.lms.constants.EmailTemplates;
import com.firom.lms.services.AuthNotificationService;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthNotificationServiceImpl implements AuthNotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendEmailVerification(
            String destinationEmail,
            String username,
            String verificationToken
    ) {

        // Set up mail sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        // Set account verification link
        String link = "http://localhost:8081/api/v1/auth/verify-email?token=" + verificationToken;

        // Set variables to Thymeleaf context in-order to replace template's placeholders
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("verificationLink", link);
        variables.put("year", Calendar.getInstance().get(Calendar.YEAR));
        Context context = new Context();
        context.setVariables(variables);

        try {
            // Set up html template
            final String templateName = EmailTemplates.EMAIL_VERIFICATION.getTemplate();
            String htmlTemplate = templateEngine.process(templateName, context);

            // Set up message
            messageHelper.setSubject(EmailTemplates.EMAIL_VERIFICATION.getSubject());
            messageHelper.setFrom("firom.khim@geekybyte.com");
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);

            // Send
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send email verification to {}", destinationEmail);
        }
    }
}
