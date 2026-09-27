package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.SaleDto;
import com.ibm.grocery.contracts.SaleRequest;
import com.ibm.grocery.core.service.SalesService;
import com.ibm.grocery.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("sales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaleResource {

    @Inject
    SalesService salesService;

    @POST
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public Response record(@Valid SaleRequest request, @Context SecurityContext securityContext) {
        SaleDto sale = salesService.record(request, securityContext.getUserPrincipal().getName());
        return Response.status(Response.Status.CREATED).entity(sale).build();
    }

    @GET
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public List<SaleDto> recent() {
        return salesService.recent();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public SaleDto get(@PathParam("id") Long id) {
        return salesService.get(id);
    }
}
