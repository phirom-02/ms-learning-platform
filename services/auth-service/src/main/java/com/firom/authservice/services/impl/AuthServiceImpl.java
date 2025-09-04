package com.firom.authservice.services.impl;

import com.firom.authservice.dto.mapper.AuthMapper;
import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.request.LoginRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.AuthenticationResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.dto.response.UserResponse;
import com.firom.authservice.entRepo.CustomUserDetails;
import com.firom.authservice.entRepo.RefreshToken;
import com.firom.authservice.entRepo.User;
import com.firom.authservice.entRepo.UserRoles;
import com.firom.authservice.exceptions.InvalidTokenException;
import com.firom.authservice.remotes.client.UserClient;
import com.firom.authservice.services.AuthService;
import com.firom.authservice.services.JwtService;
import com.firom.authservice.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Value("#{new Long('${application.jwt.access-token-validity}')}")
    private Long accessTokenValidity;

    @Value("#{new Long('${application.jwt.refresh-token-validity}')}")
    private Long refreshTokenValidity;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        // TODO: check if password matches
        // Check if password and password confirm matches
        if (!request.getPassword().matches(request.getPasswordConfirm())) {
            throw new RuntimeException("Password does not match");
        }
        // Map to CreateUserRequest
        CreateUserRequest createUserRequest = authMapper.signUpRequestToCreateUserRequest(request);
        // Encode password
        createUserRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        // Create a new user by requesting to user service
        ResponseEntity<ApiResponse<User>> response = userClient.createUser(createUserRequest);
        User user = response.getBody().getData();
        // Map user to SignUpResponse
        return authMapper.userToSignUpResponse(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        // Authenticate request
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserResponse userResponse = authMapper.usertoUserResponse(userDetails.getUser());

        Map<String, Object> claims = setTokenClaims(
                userResponse.getId(),
                userResponse.getUsername(),
                userResponse.getEmail(),
                userResponse.getRoles(),
                userDetails.getUser()
        );

        // Generate new access token
        String accessToken = jwtService.generateToken(userDetails.getUsername(), claims, accessTokenValidity);
        // Generate new refresh token
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDetails, claims);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(accessTokenValidity)
                .refreshExpiresIn(refreshToken.getRefreshTokenExpiresIn())
                .userInfo(userResponse)
                .build();
    }

    @Override
    public AuthenticationResponse refresh(String token) {
        // Validate token
        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        // TODO: Check if token blacklisted


        // Extract username from token
        String username = jwtService.getSubject(token);

        // Get user information
        ResponseEntity<ApiResponse<User>> response = userClient.getUserByUsername(username);
        User user = response.getBody().getData();
        if (user == null) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        UserResponse userResponse = authMapper.usertoUserResponse(user);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Prepare claims for new tokens
        Map<String, Object> claims = setTokenClaims(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user
        );

        // Generate new access token
        String accessToken = jwtService.generateToken(username, claims, accessTokenValidity);
        // Generate new refresh token (rotation)
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDetails, claims);

        // Invalidate old refresh token by adding it to blacklist

        // Remove old refresh token from storage

        // Store new refresh token

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(accessTokenValidity)
                .refreshExpiresIn(refreshToken.getRefreshTokenExpiresIn())
                .userInfo(userResponse)
                .build();
    }

    private Map<String, Object> setTokenClaims(String id, String username2, String email, Set<UserRoles> roles, User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        claims.put("username", username2);
        claims.put("email", email);
        claims.put("roles", roles);
        return claims;
    }
}

// TODO: Persist token into cache db and implement refresh token CRUD

