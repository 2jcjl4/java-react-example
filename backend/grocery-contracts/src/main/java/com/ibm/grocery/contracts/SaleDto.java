package com.ibm.grocery.contracts;

import java.math.BigDecimal;
import java.util.List;

public record SaleDto(
        Long id,
        String reference,
        String soldBy,
        String soldAt,
        BigDecimal totalAmount,
        List<SaleLineDto> lines) {
}
