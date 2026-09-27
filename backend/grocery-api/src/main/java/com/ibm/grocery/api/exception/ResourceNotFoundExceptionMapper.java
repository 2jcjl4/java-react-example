package com.ibm.grocery.api.exception;

import com.ibm.grocery.contracts.ApiError;
import com.ibm.grocery.core.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiError("not_found", exception.getMessage()))
                .build();
    }
}
