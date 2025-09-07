package com.firom.authservice.services;

import com.firom.authservice.dto.request.ChangePasswordRequest;
import com.firom.authservice.dto.request.LoginRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.request.VerifyEmailRequest;
import com.firom.authservice.dto.response.AuthenticationResponse;
import com.firom.authservice.dto.response.SignUpResponse;

public interface AuthService {

    SignUpResponse signUp(SignUpRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refresh(String token);

    void logout(String token);

    void logoutAllDevices(String token);

    void changePassword(String userId, ChangePasswordRequest request);

    void verifyEmail(String token);

    String requestVerifyEmail(VerifyEmailRequest request);

//    String requestVerifyEmail(VerifyEmailRequest request);
}
