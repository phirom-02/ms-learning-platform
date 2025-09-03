package com.firom.authservice.services;

import com.firom.authservice.dto.request.SignUpRequest;
import com.firom.authservice.dto.response.SignUpResponse;

public interface AuthService {

    SignUpResponse signUp(SignUpRequest request);
}
