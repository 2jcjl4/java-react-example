package com.ibm.grocery.contracts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleLineRequest(@NotNull Long itemId, @NotNull @Positive Integer quantity) {
}
