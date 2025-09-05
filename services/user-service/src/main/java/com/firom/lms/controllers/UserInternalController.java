package com.firom.lms.controllers;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdatePasswordRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/internal")
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
        var response = new ApiResponse<>(userService.getUserEntityByUsername(username));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable(name = "email") String email) {
        var response = new ApiResponse<>(userService.getUserEntityByEmail(email));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable(name = "user-id") String userId) {
        var response = new ApiResponse<>(userService.getUserEntityById(userId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{user-id}/change-password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable(name = "user-id") String userId, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{user-id}/enable")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable(name = "user-id") String userId) {
        userService.setUserEnableStatus(userId, true);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{user-id}/disable")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable(name = "user-id") String userId) {
        userService.setUserEnableStatus(userId, false);
        ApiResponse<Void> response = new ApiResponse<>(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
