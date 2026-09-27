package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.LoginRequest;
import com.ibm.grocery.contracts.LoginResponse;
import com.ibm.grocery.contracts.UserDto;
import com.ibm.grocery.core.service.AuthenticationService;
import com.ibm.grocery.core.service.UserService;
import com.ibm.grocery.domain.Roles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    UserService userService;

    @POST
    @Path("login")
    @PermitAll
    public LoginResponse login(@Valid LoginRequest request) {
        return authenticationService.login(request);
    }

    @GET
    @Path("me")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public UserDto currentUser(@Context SecurityContext securityContext) {
        return userService.getByUsername(securityContext.getUserPrincipal().getName());
    }
}
