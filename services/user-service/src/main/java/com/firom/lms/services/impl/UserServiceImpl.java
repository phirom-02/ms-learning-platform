package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.UserMapper;
import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdatePasswordRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.entRepo.UserRepository;
import com.firom.lms.entRepo.UserRoles;
import com.firom.lms.entRepo.UserSpecification;
import com.firom.lms.exceptions.CustomDuplicateKeyException;
import com.firom.lms.exceptions.InvalidRoleException;
import com.firom.lms.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        try {
            var userToCreate = userMapper.createUserRequestToEntity(request);
            return userRepository.save(userToCreate);
        } catch (Exception e) {
            throw new CustomDuplicateKeyException("This email in used: " + request.getEmail());
        }
    }

    @Override
    public Page<UserResponse> getAllUsers(
            int page,
            int size,
            String sort,
            String username,
            String email,
            String firstName,
            String lastName,
            String roles
    ) {
        // Parse roles
        List<UserRoles> mappedRoles;
        try {
            mappedRoles = roles == null
                    ? null
                    : Arrays.stream(roles.split(","))
                    .map(String::trim)
                    .map(UserRoles::valueOf)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + roles);
        }

        // Parse sort. e.g. createdAt,DESC or createdAt,desc
        String[] sortParts = sort.split(",");
        Sort.Direction sortDirection = Sort.Direction
                .fromOptionalString(sortParts[1].toUpperCase())
                .orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortParts[0]));

        // Build specification
        Specification<User> spec = UserSpecification.unrestricted()
                .and(UserSpecification.usernameContains(username))
                .and(UserSpecification.emailContains(email))
                .and(UserSpecification.firstNameContains(firstName))
                .and(UserSpecification.lastNameContains(lastName))
                .and(UserSpecification.hasAnyRole(mappedRoles));

        // Query users data + pagination
        Page<User> users = userRepository.findAll(spec, pageable);

        List<UserResponse> response = users.getContent().stream()
                .map(userMapper::entityToUserResponse)
                .toList();

        return new PageImpl<>(response, pageable, users.getTotalElements());
    }

    @Override
    public UserResponse getUserById(String userId) {
        return userMapper.entityToUserResponse(getUserEntityById(userId));
    }

    @Override
    public User getUserEntityById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
    }

    @Override
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        var user = getUserEntityById(userId);
        var userToUpdate = userMapper.updateUserRequestToEntity(request, user);
        var updatedUser = userRepository.save(userToUpdate);
        return userMapper.entityToUserResponse(updatedUser);
    }

    @Override
    public void deleteUserById(String userId) {
        getUserEntityById(userId);
        userRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No user found with username: " + username));
    }

    @Override
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No user found with email: " + email));
    }

    @Override
    public User updatePassword(String userId, UpdatePasswordRequest request) {
        var user = getUserEntityById(userId);
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    @Override
    public User setUserEnableStatus(String userId, boolean value) {
        var user = getUserEntityById(userId);
        user.setEnabled(value);
        return userRepository.save(user);
    }
}
