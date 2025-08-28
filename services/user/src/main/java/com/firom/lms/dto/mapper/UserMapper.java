package com.firom.lms.dto.mapper;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User createUserRequestToEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    public UserResponse entityToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User updateUserRequestToEntity(UpdateUserRequest request, User user) {
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        return user;
    }
}
