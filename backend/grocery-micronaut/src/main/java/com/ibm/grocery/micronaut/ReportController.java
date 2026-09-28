package com.ibm.grocery.micronaut;

import com.ibm.grocery.contracts.DashboardSummary;
import com.ibm.grocery.contracts.LowStockItemDto;
import com.ibm.grocery.core.service.ReportService;
import com.ibm.grocery.domain.Roles;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import java.util.List;

@Controller("/api/reports")
@Secured({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Get("/low-stock")
    public List<LowStockItemDto> lowStock() {
        return reportService.lowStock();
    }

    @Get("/dashboard")
    public DashboardSummary dashboard() {
        return reportService.dashboard();
    }
}
