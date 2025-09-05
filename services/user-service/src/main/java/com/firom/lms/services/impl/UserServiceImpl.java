package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.UserMapper;
import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdatePasswordRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.entRepo.UserRepository;
import com.firom.lms.exceptions.CustomDuplicateKeyException;
import com.firom.lms.exceptions.EntityNotFoundException;
import com.firom.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        } catch (DuplicateKeyException e) {
            throw new CustomDuplicateKeyException("This email in used: " + request.getEmail());
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::entityToUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(String userId) {
        return userMapper.entityToUserResponse(getUserEntityById(userId));
    }

    @Override
    public User getUserEntityById(String userId) {
        return userRepository.findById(userId)
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
        userRepository.deleteById(userId);
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
