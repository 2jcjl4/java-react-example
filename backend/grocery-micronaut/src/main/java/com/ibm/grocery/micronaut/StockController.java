package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.StockAdjustmentRequest;
import com.ibm.grocery.contracts.StockMovementDto;
import com.ibm.grocery.core.service.StockService;
import com.ibm.grocery.domain.Roles;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Post("/items/{itemId}/movements")
    @Secured({Roles.ADMIN, Roles.MANAGER})
    public HttpResponse<StockMovementDto> adjust(@io.micronaut.http.annotation.PathVariable Long itemId, @Body @Valid StockAdjustmentRequest request,
                                                  Authentication authentication) {
        return HttpResponse.created(stockService.adjust(itemId, request, authentication.getName()));
    }

    @Get("/movements")
    @Secured({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public List<StockMovementDto> history(@QueryValue @Nullable Long itemId) {
        return stockService.history(itemId);
    }
}
