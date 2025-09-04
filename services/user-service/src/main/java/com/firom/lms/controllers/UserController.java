package com.firom.lms.controllers;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        var response = new ApiResponse<>(userService.getAllUsers());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable(name = "user-id") String userId) {
        var response = new ApiResponse<>(userService.getUserById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{user-id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable(name = "user-id") String userId, @RequestBody UpdateUserRequest request) {
        var response = new ApiResponse<>(userService.updateUser(userId, request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable(name = "user-id") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
