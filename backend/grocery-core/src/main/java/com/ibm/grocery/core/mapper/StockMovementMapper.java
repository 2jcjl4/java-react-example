package com.ibm.grocery.core.mapper;

import com.ibm.grocery.contracts.StockMovementDto;
import com.ibm.grocery.domain.StockMovement;
import java.time.format.DateTimeFormatter;

public final class StockMovementMapper {

    private StockMovementMapper() {
    }

    public static StockMovementDto toDto(StockMovement movement) {
        return new StockMovementDto(
                movement.getId(),
                movement.getItem().getId(),
                movement.getItem().getSku(),
                movement.getItem().getName(),
                movement.getMovementType().name(),
                movement.getQuantityDelta(),
                movement.getResultingQuantity(),
                movement.getReference(),
                movement.getNote(),
                movement.getPerformedBy(),
                DateTimeFormatter.ISO_INSTANT.format(movement.getOccurredAt()));
    }
}
