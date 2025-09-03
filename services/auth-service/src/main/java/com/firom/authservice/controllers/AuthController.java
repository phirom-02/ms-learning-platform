package com.firom.authservice.controllers;

import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signup(@RequestBody @Valid SignUpRequest request) {

        ApiResponse<SignUpResponse> response = new ApiResponse<>(authService.signUp(request));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
