package com.ibm.grocery.contracts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SaleRequest(@NotEmpty @Valid List<SaleLineRequest> lines) {
}
