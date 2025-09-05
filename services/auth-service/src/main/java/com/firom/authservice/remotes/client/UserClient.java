package com.firom.authservice.remotes.client;

import com.firom.authservice.dto.request.ChangeUserPasswordRequest;
import com.firom.authservice.dto.request.CreateUserRequest;
import com.firom.authservice.dto.response.ApiResponse;
import com.firom.authservice.entRepo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        url = "${application.remotes.user-url}/internal"
)
public interface UserClient {

    @PostMapping
    ResponseEntity<ApiResponse<User>> createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<User>> getUserById(@PathVariable String id);

    @GetMapping("/username/{username}")
    ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable("username") String username);

    @PatchMapping("/{user-id}/change-password")
    ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable("user-id") String userId, @RequestBody ChangeUserPasswordRequest request);
}
