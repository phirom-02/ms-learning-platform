package com.firom.authservice.services.impl;

import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.entRepo.CustomUserDetails;
import com.firom.authservice.entRepo.User;
import com.firom.authservice.remotes.client.UserClient;
import com.firom.authservice.services.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserClient userClient;

    public CustomUserDetailsServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<ApiResponse<User>> response = userClient.getUserByUsername(username);
        return new CustomUserDetails(response.getBody().getData());
    }
}
