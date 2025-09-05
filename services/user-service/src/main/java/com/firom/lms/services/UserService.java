package com.firom.lms.services;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdatePasswordRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    User getUserEntityById(String userId);

    UserResponse updateUser(String userId, UpdateUserRequest request);

    void deleteUserById(String userId);

    User getUserEntityByUsername(String username);

    User getUserEntityByEmail(String email);

    User updatePassword(String userId, UpdatePasswordRequest request);

    User setUserEnableStatus(String userId, boolean value);
}
