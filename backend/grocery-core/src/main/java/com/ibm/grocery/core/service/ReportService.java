package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.DashboardSummary;
import com.ibm.grocery.contracts.LowStockItemDto;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.SaleRepository;
import com.ibm.grocery.domain.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@ApplicationScoped
public class ReportService {

    @Inject
    ItemRepository itemRepository;

    @Inject
    SaleRepository saleRepository;

    public List<LowStockItemDto> lowStock() {
        return itemRepository.findLowStock().stream().map(ReportService::toLowStockDto).toList();
    }

    public DashboardSummary dashboard() {
        Instant startOfDay = LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC).toInstant();
        return new DashboardSummary(
                itemRepository.countActive(),
                itemRepository.findLowStock().size(),
                itemRepository.totalUnitsOnHand(),
                itemRepository.stockValue(),
                saleRepository.countSince(startOfDay),
                saleRepository.revenueSince(startOfDay));
    }

    private static LowStockItemDto toLowStockDto(Item item) {
        return new LowStockItemDto(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getCategory(),
                item.getQuantityOnHand(),
                item.getReorderLevel(),
                Math.max(0, item.getReorderLevel() - item.getQuantityOnHand()));
    }
}
