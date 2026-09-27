package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.ItemDto;
import com.ibm.grocery.contracts.ItemRequest;
import com.ibm.grocery.core.service.ItemService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject
    ItemService itemService;

    @GET
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public List<ItemDto> search(@QueryParam("search") String search,
                                @QueryParam("includeInactive") boolean includeInactive) {
        return itemService.search(search, includeInactive);
    }

    @GET
    @Path("{id}")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public ItemDto get(@PathParam("id") Long id) {
        return itemService.get(id);
    }

    @POST
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response create(@Valid ItemRequest request) {
        return Response.status(Response.Status.CREATED).entity(itemService.create(request)).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public ItemDto update(@PathParam("id") Long id, @Valid ItemRequest request) {
        return itemService.update(id, request);
    }

    @POST
    @Path("{id}/activate")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public ItemDto activate(@PathParam("id") Long id) {
        return itemService.setActive(id, true);
    }

    @POST
    @Path("{id}/deactivate")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public ItemDto deactivate(@PathParam("id") Long id) {
        return itemService.setActive(id, false);
    }
}
