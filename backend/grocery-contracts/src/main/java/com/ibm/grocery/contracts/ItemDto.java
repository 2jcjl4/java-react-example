package com.ibm.grocery.contracts;

import java.math.BigDecimal;

public record ItemDto(
        Long id,
        String sku,
        String name,
        String category,
        BigDecimal unitPrice,
        int quantityOnHand,
        int reorderLevel,
        boolean active,
        boolean lowStock) {
}
