package com.ibm.grocery.api.resource;

import com.ibm.grocery.contracts.DashboardSummary;
import com.ibm.grocery.contracts.LowStockItemDto;
import com.ibm.grocery.core.service.ReportService;
import com.ibm.grocery.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

    @Inject
    ReportService reportService;

    @GET
    @Path("low-stock")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public List<LowStockItemDto> lowStock() {
        return reportService.lowStock();
    }

    @GET
    @Path("dashboard")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.CASHIER})
    public DashboardSummary dashboard() {
        return reportService.dashboard();
    }
}
