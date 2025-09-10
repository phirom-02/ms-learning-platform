package com.firom.lms.constants;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    COURSE_ENROLLMENT_NOTICE("enrollment-notice.html", "Course Enrollment Notice"),
    EMAIL_VERIFICATION("email-verification.html", "Verify your email address");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
