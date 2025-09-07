package com.firom.authservice.services.impl;

import com.firom.authservice.configs.security.CustomUserDetails;
import com.firom.authservice.dto.mapper.AuthMapper;
import com.firom.authservice.dto.request.*;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.AuthenticationResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.dto.response.UserResponse;
import com.firom.authservice.entRepo.RefreshToken;
import com.firom.authservice.entRepo.User;
import com.firom.authservice.entRepo.UserRoles;
import com.firom.authservice.exceptions.*;
import com.firom.authservice.producers.AuthProducer;
import com.firom.authservice.producers.EmailVerificationMessage;
import com.firom.authservice.remotes.client.UserClient;
import com.firom.authservice.services.AuthService;
import com.firom.authservice.services.JwtService;
import com.firom.authservice.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private final AuthProducer authProducer;

    @Value("#{new Long('${application.jwt.access-token-validity}')}")
    private Long accessTokenValidity;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        // Check if password and password confirm matches
        if (!request.getPassword().matches(request.getPasswordConfirm())) {
            throw new SignupException("Password does not match");
        }

        // Map to CreateUserRequest
        CreateUserRequest createUserRequest = authMapper.signUpRequestToCreateUserRequest(request);

        // Encode password
        createUserRequest.setPassword(passwordEncoder.encode(request.getPassword()));

        // Create a new user by requesting to user service
        ResponseEntity<ApiResponse<User>> response = userClient.createUser(createUserRequest);
        User user = Objects.requireNonNull(response.getBody()).getData();

        // Generate email verification token and
        String verificationToken = jwtService.generateToken(
                user.getUsername(),
                setTokenClaims(user.getId(), user.getEmail(), user.getRoles()),
                accessTokenValidity
        );

        // Send email verification message
        authProducer.sendEmailVerification(EmailVerificationMessage.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .verificationToken(verificationToken)
                .build());

        // Return response
        return authMapper.userToSignUpResponse(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        // Authenticate request
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new LoginException("Invalid username or password");
        } catch (DisabledException e) {
            throw new AccountStateException("Account is disabled");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserResponse userResponse = authMapper.usertoUserResponse(userDetails.getUser());

        Map<String, Object> claims = setTokenClaims(
                userResponse.getId(),
                userResponse.getEmail(),
                userResponse.getRoles()
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
        // Check if given token exist in db(cache)
        RefreshToken refreshToken = refreshTokenService.getTokenByToken(token);
        if (refreshToken == null) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        // Validate token
        if (!jwtService.isTokenValid(token) && refreshTokenService.isTokenExpired(refreshToken)) {
            // Remove token from db if expired
            refreshTokenService.deleteByToken(token);
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Extract username from token
        String username = jwtService.getSubject(token);

        // Get user information
        User user;
        ResponseEntity<ApiResponse<User>> response = userClient.getUserByUsername(username);
        user = Objects.requireNonNull(Objects.requireNonNull(response.getBody())).getData();
        if (user == null) {
            throw new UserNotFoundException("No user found with username: " + username);
        }
        UserResponse userResponse = authMapper.usertoUserResponse(user);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Prepare claims for new tokens
        Map<String, Object> claims = setTokenClaims(
                user.getId(),
                user.getEmail(),
                user.getRoles()
        );

        // Generate new access token
        String accessToken = jwtService.generateToken(username, claims, accessTokenValidity);
        // Generate new refresh token (rotation)
        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken(userDetails, claims);
        // Remove given refresh token from storage to prevent token reuse
        refreshTokenService.deleteByToken(token);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(accessTokenValidity)
                .refreshExpiresIn(newRefreshToken.getRefreshTokenExpiresIn())
                .userInfo(userResponse)
                .build();
    }

    @Override
    public void logout(String token) {
        // Check if given token exist in db(cache)
        RefreshToken refreshToken = refreshTokenService.getTokenByToken(token);
        if (refreshToken == null) {
            throw new InvalidTokenException("Invalid or refresh token");
        }
        // Remove token from db
        refreshTokenService.deleteByToken(token);
    }

    @Override
    public void logoutAllDevices(String token) {
        // Check if given token exist in db(cache)
        RefreshToken refreshToken = refreshTokenService.getTokenByToken(token);
        if (refreshToken == null) {
            throw new InvalidTokenException("Invalid or refresh token");
        }
        // Remove token related to the username from db
        refreshTokenService.deleteTokensByUsername(refreshToken.getUsername());
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {
        // Check if passwords match
        if (!request.getPassword().matches(request.getPasswordConfirm())) {
            throw new SignupException("Password does not match");
        }
        // Encode new password
        String newPassword = passwordEncoder.encode(request.getPassword());
        // Retrieve user in-order to get the previous password
        ResponseEntity<ApiResponse<User>> response = userClient.getUserById(userId);
        User user = Objects.requireNonNull(response.getBody()).getData();
        // Check if new password is the same as old password
        // If true throw a PasswordChangeException
        if (newPassword.matches(user.getPassword())) {
            throw new PasswordChangeException("New password cannot be the same as the old password");
        }
        // Map data
        ChangeUserPasswordRequest changeUserPasswordRequest = new ChangeUserPasswordRequest(newPassword);
        // Make http request password change to user-service
        userClient.updatePassword(userId, changeUserPasswordRequest);
    }

    @Override
    public void verifyEmail(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired token");
        }
        // Extract userid and email from token
        String userId = jwtService.extractClaim(token, "userId", String.class);
        String email = jwtService.extractClaim(token, "email", String.class);
        // Check if user exists in db
        ResponseEntity<ApiResponse<User>> response = userClient.getUserById(userId);
        User user = Objects.requireNonNull(response.getBody()).getData();
        // Compare if emails matches
        if (!user.getEmail().equals(email)) {
            throw new EmailVerificationException("Invalid email address");
        }
        // Enable user after email verification
        userClient.enableUser(userId);
    }

    @Override
    public String requestVerifyEmail(VerifyEmailRequest request) {
        ResponseEntity<ApiResponse<User>> response = userClient.getUserByEmail(request.getEmail());
        User user = Objects.requireNonNull(response.getBody()).getData();
        Map<String, Object> claims = setTokenClaims(user.getId(), user.getEmail(), user.getRoles());
        // TODO: send message to notification service
        return jwtService.generateToken(user.getUsername(), claims, accessTokenValidity);
    }

    private Map<String, Object> setTokenClaims(String id, String email, Set<UserRoles> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        claims.put("email", email);
        claims.put("roles", roles);
        return claims;
    }
}

