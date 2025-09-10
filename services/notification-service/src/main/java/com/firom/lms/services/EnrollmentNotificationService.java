package com.firom.lms.services;

public interface EnrollmentNotificationService {
    void sendEnrollmentNotice(String courseTitle, String studentEmail, String studentUsername, String instructorUsername);
}
