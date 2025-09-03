package com.firom.lms.dto.mapper;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.entRepo.UserRoles;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    public User createUserRequestToEntity(CreateUserRequest request) {
        Set<UserRoles> roles = new HashSet<>();
        request.getRoles().forEach(role -> roles
                .add(UserRoles.valueOf(role))
        );

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(roles)
                .build();
    }

    public UserResponse entityToUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles())
                .build();
    }

    public User updateUserRequestToEntity(UpdateUserRequest request, User user) {
//        if (request.getEmail() != null) {
//            user.setEmail(request.getEmail());
//        }
//        if (request.getName() != null) {
//            user.setName(request.getName());
//        }

        return user;
    }
}
