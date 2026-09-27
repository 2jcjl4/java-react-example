package com.ibm.grocery.api.exception;

import com.ibm.grocery.contracts.ApiError;
import com.ibm.grocery.core.exception.BusinessRuleException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessRuleExceptionMapper implements ExceptionMapper<BusinessRuleException> {

    @Override
    public Response toResponse(BusinessRuleException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiError("business_rule_violation", exception.getMessage()))
                .build();
    }
}
