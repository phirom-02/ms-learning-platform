package com.firom.authservice.services.impl;

import com.firom.authservice.dto.mapper.AuthMapper;
import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.dto.response.SignUpResponse;
import com.firom.authservice.entRepo.User;
import com.firom.authservice.remotes.client.UserClient;
import com.firom.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final AuthMapper authMapper;


    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        CreateUserRequest createUserRequest = authMapper.signUpRequestToCreateUserRequest(request);
        ResponseEntity<ApiResponse<User>> response = userClient.createUser(createUserRequest);
        User user = response.getBody().getData();
        return authMapper.userToSignUpResponse(user);
    }
}
