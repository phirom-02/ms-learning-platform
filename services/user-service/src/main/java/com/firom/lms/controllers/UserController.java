package com.firom.lms.controllers;

import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt,DESC") String sort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String roles
    ) {
        var response = new ApiResponse<>(userService.getAllUsers(
                page,
                size,
                sort,
                username,
                email,
                firstName,
                lastName,
                roles)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable(name = "user-id") UUID userId) {
        var response = new ApiResponse<>(userService.getUserById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
