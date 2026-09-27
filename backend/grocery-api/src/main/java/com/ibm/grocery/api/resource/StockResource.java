package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.StockAdjustmentRequest;
import com.ibm.grocery.contracts.StockMovementDto;
import com.ibm.grocery.core.service.StockService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("stock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockResource {

    @Inject
    StockService stockService;

    @POST
    @Path("items/{itemId}/movements")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response adjust(@PathParam("itemId") Long itemId,
                           @Valid StockAdjustmentRequest request,
                           @Context SecurityContext securityContext) {
        StockMovementDto movement =
                stockService.adjust(itemId, request, securityContext.getUserPrincipal().getName());
        return Response.status(Response.Status.CREATED).entity(movement).build();
    }

    @GET
    @Path("movements")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public List<StockMovementDto> history(@QueryParam("itemId") Long itemId) {
        return stockService.history(itemId);
    }
}
