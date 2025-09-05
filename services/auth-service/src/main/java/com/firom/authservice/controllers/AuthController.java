package com.firom.authservice.controllers;

import com.firom.authservice.dto.request.ChangePasswordRequest;
import com.firom.authservice.dto.request.LoginRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.request.VerifyEmailRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.AuthenticationResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.producers.AuthMessageProducer;
import com.firom.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMessageProducer authMessageProducer;

    /**
     * Signup endpoint
     *
     * @param request
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signup(@RequestBody @Valid SignUpRequest request) {
        // TODO: send email verification message to notification-service
        ApiResponse<SignUpResponse> response = new ApiResponse<>(authService.signUp(request));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Verify user email
     *
     * @param email
     * @param userId
     * @return
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestAttribute String email, @RequestAttribute String userId) {
        authService.verifyEmail(userId, email);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/verify-email/request")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        String token = authService.requestVerifyEmail(request);
        ApiResponse<String> response = new ApiResponse<>(token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Login endpoint
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(authService.login(request));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Refresh(renew) token endpoint
     *
     * @param token
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(@RequestHeader("X-Refresh-Token") String token) {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(authService.refresh(token));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Logout endpoint
     *
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("X-Refresh-Token") String token) {
        authService.logout(token);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Logout all devices endpoint
     *
     * @param token
     * @return
     */
    @PostMapping("/logout-all-devices")
    public ResponseEntity<ApiResponse<Void>> logoutAllDevices(@RequestHeader("X-Refresh-Token") String token) {
        authService.logoutAllDevices(token);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    // TODO: Issue password reset endpoint
    // TODO: Instead of returning token send token to notification-service and let notification-service send to user email

    /**
     * Request password reset endpoint
     *
     * @return
     */
    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse<String>> requestPasswordReset() {
        ApiResponse<String> token = new ApiResponse<>("");
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }

    // TODO: Change password endpoint
    // TODO: Decide should invalidate all refresh token(logout all devices)

    /**
     * Update password endpoint
     *
     * @param request
     * @param userId
     * @return
     */
    @PostMapping("/password-reset/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request, @RequestAttribute("userId") String userId) {
        authService.changePassword(userId, request);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/test-message")
    public ResponseEntity<ApiResponse<Void>> testMessage() {
        authMessageProducer.sendAuthNotification("Message from auth service");
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
