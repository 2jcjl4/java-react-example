package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.CreateUserRequest;
import com.ibm.grocery.contracts.ResetPasswordRequest;
import com.ibm.grocery.contracts.UpdateUserRequest;
import com.ibm.grocery.contracts.UserDto;
import com.ibm.grocery.core.service.UserService;
import com.ibm.grocery.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed(Roles.ADMIN)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public List<UserDto> list() {
        return userService.list();
    }

    @POST
    public Response create(@Valid CreateUserRequest request) {
        return Response.status(Response.Status.CREATED).entity(userService.create(request)).build();
    }

    @PUT
    @Path("{id}")
    public UserDto update(@PathParam("id") Long id,
                          @Valid UpdateUserRequest request,
                          @Context SecurityContext securityContext) {
        return userService.update(id, request, securityContext.getUserPrincipal().getName());
    }

    @PUT
    @Path("{id}/password")
    public UserDto resetPassword(@PathParam("id") Long id, @Valid ResetPasswordRequest request) {
        return userService.resetPassword(id, request);
    }
}
