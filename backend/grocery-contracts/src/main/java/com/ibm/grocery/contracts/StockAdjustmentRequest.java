package com.ibm.grocery.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StockAdjustmentRequest(
        @NotBlank String movementType,
        @NotNull @Positive Integer quantity,
        @Size(max = 250) String note) {
}
