package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.UserMapper;
import com.firom.lms.dto.request.CreateUserRequest;
import com.firom.lms.dto.request.UpdateUserRequest;
import com.firom.lms.dto.response.UserResponse;
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
    public UserResponse createUser(CreateUserRequest request) {
        try {
            var userToCreate = userMapper.createUserRequestToEntity(request);
            var createdUser = userRepository.save(userToCreate);
            return userMapper.entityToUserResponse(createdUser);
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
        return userRepository.findById(userId)
                .map(userMapper::entityToUserResponse)
                .orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        var userToUpdate = userMapper.updateUserRequestToEntity(request, user);

        var updatedUser = userRepository.save(userToUpdate);

        return userMapper.entityToUserResponse(updatedUser);
    }

    @Override
    public void deleteUserById(String userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }
}
