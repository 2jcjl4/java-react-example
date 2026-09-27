package com.ibm.grocery.contracts;

public record LowStockItemDto(
        Long id,
        String sku,
        String name,
        String category,
        int quantityOnHand,
        int reorderLevel,
        int shortfall) {
}
