package com.firom.lms.controllers;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody CreateUserRequest request) {
        var response = new ApiResponse<>(userService.createUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable(name = "username") String username) {
        var response = new ApiResponse<>(userService.getUserByUsername(username));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
