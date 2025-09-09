package com.firom.authservice.controllers;

import com.firom.authservice.dto.request.ChangePasswordRequest;
import com.firom.authservice.dto.request.LoginRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.request.VerifyEmailRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.AuthenticationResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.dto.response.UserResponse;
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
     * @param token
     * @return Html page that let a user know their account has been verified
     */
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        return authService.verifyEmail(token);
    }

    @PostMapping("/verify-email/request")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        authService.requestVerifyEmail(request);
        ApiResponse<Void> response = new ApiResponse<>(null);
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
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            @RequestAttribute("userId") String userId
    ) {
        authService.changePassword(userId, request);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserDetails(@RequestAttribute("userId") String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>(authService.getCurrentUserDetails(userId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
