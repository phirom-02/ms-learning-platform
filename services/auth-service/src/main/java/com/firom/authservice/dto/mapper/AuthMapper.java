package com.firom.authservice.dto.mapper;

import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.dto.response.UserResponse;
import com.firom.authservice.entRepo.User;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {

    public CreateUserRequest signUpRequestToCreateUserRequest(SignUpRequest request) {
        return CreateUserRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .build();
    }

    public SignUpResponse userToSignUpResponse(User user) {
        return SignUpResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserResponse usertoUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles())
                .build();
    }
}
