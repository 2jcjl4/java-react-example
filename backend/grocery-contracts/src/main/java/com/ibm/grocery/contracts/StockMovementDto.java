package com.ibm.grocery.contracts;

public record StockMovementDto(
        Long id,
        Long itemId,
        String sku,
        String itemName,
        String movementType,
        int quantityDelta,
        int resultingQuantity,
        String reference,
        String note,
        String performedBy,
        String occurredAt) {
}
