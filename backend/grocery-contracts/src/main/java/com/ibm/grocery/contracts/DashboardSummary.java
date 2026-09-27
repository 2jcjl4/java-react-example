package com.ibm.grocery.contracts;

import java.math.BigDecimal;

public record DashboardSummary(
        long activeItems,
        long lowStockItems,
        long totalUnitsOnHand,
        BigDecimal stockValue,
        long salesToday,
        BigDecimal revenueToday) {
}
