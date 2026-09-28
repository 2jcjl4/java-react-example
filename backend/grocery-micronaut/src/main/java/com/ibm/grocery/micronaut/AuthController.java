package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.LoginRequest;
import com.ibm.grocery.contracts.LoginResponse;
import com.ibm.grocery.core.mapper.UserMapper;
import com.ibm.grocery.core.repository.UserRepository;
import com.ibm.grocery.core.security.PasswordHasher;
import com.ibm.grocery.core.service.UserService;
import com.ibm.grocery.core.exception.AuthenticationFailedException;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.validation.Valid;
import java.util.Map;

@Controller("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public AuthController(UserRepository userRepository, UserService userService,
                          PasswordHasher passwordHasher, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public LoginResponse login(@Body @Valid LoginRequest request) {
        var user = userRepository.findByUsername(request.username())
                .filter(candidate -> candidate.isActive() && passwordHasher.matches(request.password(), candidate.getPasswordHash()))
                .orElseThrow(() -> new AuthenticationFailedException("Invalid username or password"));

        Authentication authentication = Authentication.build(
                user.getUsername(),
                java.util.List.of(user.getRole().name()),
                Map.of("fullName", user.getFullName()));
        String token = tokenGenerator.generateToken(authentication, 28800)
                .orElseThrow(() -> new IllegalStateException("Unable to issue access token"));
        return new LoginResponse(token, 28800, UserMapper.toDto(user));
    }

    @Get("/me")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public com.ibm.grocery.contracts.UserDto currentUser(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }
}
