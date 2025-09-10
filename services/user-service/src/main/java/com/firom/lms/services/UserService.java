package com.firom.lms.services;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdatePasswordRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    User createUser(CreateUserRequest request);

    Page<UserResponse> getAllUsers(int page,
                                   int size,
                                   String sort,
                                   String username,
                                   String email,
                                   String firstName,
                                   String lastName,
                                   String roles);

    UserResponse getUserById(UUID userId);

    User getUserEntityById(UUID userId);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void deleteUserById(UUID userId);

    User getUserEntityByUsername(String username);

    User getUserEntityByEmail(String email);

    User updatePassword(UUID userId, UpdatePasswordRequest request);

    User setUserEnableStatus(UUID userId, boolean value);
}
