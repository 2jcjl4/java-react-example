package com.ibm.grocery.api.exception;

import com.ibm.grocery.contracts.ApiError;
import com.ibm.grocery.core.exception.AuthenticationFailedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthenticationFailedExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ApiError("authentication_failed", exception.getMessage()))
                .build();
    }
}
