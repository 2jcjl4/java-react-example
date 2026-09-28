package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.ItemDto;
import com.ibm.grocery.contracts.ItemRequest;
import com.ibm.grocery.core.service.ItemService;
import com.ibm.grocery.domain.Roles;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api/items")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Get
    public List<ItemDto> search(@QueryValue(defaultValue = "") String search,
                                @QueryValue(defaultValue = "false") boolean includeInactive) {
        return itemService.search(search, includeInactive);
    }

    @Get("/{id}")
    public ItemDto get(@io.micronaut.http.annotation.PathVariable Long id) {
        return itemService.get(id);
    }

    @Post
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public HttpResponse<ItemDto> create(@Body @Valid ItemRequest request) {
        return HttpResponse.created(itemService.create(request));
    }

    @Put("/{id}")
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public ItemDto update(@io.micronaut.http.annotation.PathVariable Long id, @Body @Valid ItemRequest request) {
        return itemService.update(id, request);
    }

    @Post("/{id}/activate")
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public ItemDto activate(@io.micronaut.http.annotation.PathVariable Long id) {
        return itemService.setActive(id, true);
    }

    @Post("/{id}/deactivate")
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public ItemDto deactivate(@io.micronaut.http.annotation.PathVariable Long id) {
        return itemService.setActive(id, false);
    }
}
