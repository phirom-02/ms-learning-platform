package com.firom.authservice.remotes.client;

import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.entRepo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        url = "${application.remote.user-url}"
)
public interface UserClient {

    @PostMapping
    ResponseEntity<ApiResponse<User>> createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/username/{username}")
    ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable("username") String username);
}
