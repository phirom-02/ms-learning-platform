package com.firom.authservice.remotes.impl;

import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.entRepo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserClientImpl {


    @Value("${application.oauth2.client-id}")
    private String clientId;

    @Value("${application.oauth2.client-secret}")
    private String clientSecret;


//    @Override
//    public ResponseEntity<ApiResponse<User>> createUser(CreateUserRequest request) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponse<User>> getUserByUsername(String username) {
//        return null;
//    }
}
