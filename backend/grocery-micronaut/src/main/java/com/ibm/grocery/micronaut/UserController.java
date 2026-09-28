package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.CreateUserRequest;
import com.ibm.grocery.contracts.ResetPasswordRequest;
import com.ibm.grocery.contracts.UpdateUserRequest;
import com.ibm.grocery.contracts.UserDto;
import com.ibm.grocery.core.service.UserService;
import com.ibm.grocery.domain.Roles;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api/users")
@Secured(Roles.ADMIN)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get
    public List<UserDto> list() {
        return userService.list();
    }

    @Post
    public HttpResponse<UserDto> create(@Body @Valid CreateUserRequest request) {
        return HttpResponse.created(userService.create(request));
    }

    @Put("/{id}")
    public UserDto update(@io.micronaut.http.annotation.PathVariable Long id, @Body @Valid UpdateUserRequest request, Authentication authentication) {
        return userService.update(id, request, authentication.getName());
    }

    @Put("/{id}/password")
    public UserDto resetPassword(@io.micronaut.http.annotation.PathVariable Long id, @Body @Valid ResetPasswordRequest request) {
        return userService.resetPassword(id, request);
    }
}
