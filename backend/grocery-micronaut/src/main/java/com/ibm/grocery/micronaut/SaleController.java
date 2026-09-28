package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.SaleDto;
import com.ibm.grocery.contracts.SaleRequest;
import com.ibm.grocery.core.service.SalesService;
import com.ibm.grocery.domain.Roles;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api/sales")
public class SaleController {

    private final SalesService salesService;

    public SaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @Post
    @Secured({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public HttpResponse<SaleDto> record(@Body @Valid SaleRequest request, Authentication authentication) {
        return HttpResponse.created(salesService.record(request, authentication.getName()));
    }

    @Get
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public List<SaleDto> recent() {
        return salesService.recent();
    }

    @Get("/{id}")
    @Secured({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public SaleDto get(@io.micronaut.http.annotation.PathVariable Long id) {
        return salesService.get(id);
    }
}
