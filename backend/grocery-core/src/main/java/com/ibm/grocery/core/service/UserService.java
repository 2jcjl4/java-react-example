package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.CreateUserRequest;
import com.ibm.grocery.contracts.ResetPasswordRequest;
import com.ibm.grocery.contracts.UpdateUserRequest;
import com.ibm.grocery.contracts.UserDto;
import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.exception.ResourceNotFoundException;
import com.ibm.grocery.core.mapper.UserMapper;
import com.ibm.grocery.core.repository.UserRepository;
import com.ibm.grocery.core.security.PasswordHasher;
import com.ibm.grocery.domain.AppUser;
import com.ibm.grocery.domain.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHasher passwordHasher;

    public List<UserDto> list() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    @Transactional
    public UserDto create(CreateUserRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(existing -> {
            throw new BusinessRuleException("Username " + request.username() + " is already taken");
        });

        AppUser user = new AppUser();
        user.setUsername(request.username().trim().toLowerCase(Locale.ROOT));
        user.setFullName(request.fullName().trim());
        user.setRole(parseRole(request.role()));
        user.setPasswordHash(passwordHasher.hash(request.password()));
        user.setActive(true);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(Long id, UpdateUserRequest request, String actingUsername) {
        AppUser user = require(id);
        Role newRole = parseRole(request.role());

        boolean demotingSelf = user.getUsername().equalsIgnoreCase(actingUsername)
                && (newRole != Role.ADMIN || !request.active());
        if (demotingSelf) {
            throw new BusinessRuleException("You cannot remove your own administrator access");
        }

        user.setFullName(request.fullName().trim());
        user.setRole(newRole);
        user.setActive(request.active());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto resetPassword(Long id, ResetPasswordRequest request) {
        AppUser user = require(id);
        user.setPasswordHash(passwordHasher.hash(request.newPassword()));
        return UserMapper.toDto(userRepository.save(user));
    }

    private AppUser require(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private Role parseRole(String value) {
        try {
            return Role.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BusinessRuleException("Unknown role: " + value);
        }
    }
}
