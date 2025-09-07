package com.firom.lms.services;

public interface AuthNotificationService {

    void sendEmailVerification(String destinationEmail, String username, String verificationToken);
}
