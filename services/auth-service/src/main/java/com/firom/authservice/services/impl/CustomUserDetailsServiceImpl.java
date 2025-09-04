package com.firom.authservice.services.impl;

import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.configs.security.CustomUserDetails;
import com.firom.authservice.entRepo.User;
import com.firom.authservice.exceptions.BusinessException;
import com.firom.authservice.remotes.client.UserClient;
import com.firom.authservice.services.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserClient userClient;

    public CustomUserDetailsServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<ApiResponse<User>> response = userClient.getUserByUsername(username);
        try {
            return new CustomUserDetails(Objects.requireNonNull(response.getBody()).getData());
        } catch (Exception e) {
            throw new BusinessException("Invalid username");
        }
    }
}
