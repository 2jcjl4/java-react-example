package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.LoginRequest;
import com.ibm.grocery.contracts.LoginResponse;
import com.ibm.grocery.core.exception.AuthenticationFailedException;
import com.ibm.grocery.core.mapper.UserMapper;
import com.ibm.grocery.core.repository.UserRepository;
import com.ibm.grocery.core.security.JsonWebTokenIssuer;
import com.ibm.grocery.core.security.PasswordHasher;
import com.ibm.grocery.domain.AppUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthenticationService {

    private static final String GENERIC_FAILURE = "Invalid username or password";

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Inject
    JsonWebTokenIssuer tokenIssuer;

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new AuthenticationFailedException(GENERIC_FAILURE));

        if (!user.isActive() || !passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new AuthenticationFailedException(GENERIC_FAILURE);
        }

        return new LoginResponse(
                tokenIssuer.issueFor(user),
                tokenIssuer.expirySeconds(),
                UserMapper.toDto(user));
    }
}
