package com.firom.lms.dto.mapper;

import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.constants.UserRoles;
import com.firom.lms.exceptions.InvalidRoleException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User createUserRequestToEntity(CreateUserRequest request) {

        List<UserRoles> roles = request.getRoles().stream()
                .map(role ->
                        {
                            try {
                                return UserRoles.valueOf(role);
                            } catch (IllegalArgumentException e) {
                                throw new InvalidRoleException("Invalid role: " + request.getRoles());
                            }
                        }
                ).toList();

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
                .id(user.getId())
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
        return user;
    }
}
