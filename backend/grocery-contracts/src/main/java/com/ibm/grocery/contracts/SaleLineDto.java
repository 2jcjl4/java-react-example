package com.ibm.grocery.contracts;

import java.math.BigDecimal;

public record SaleLineDto(
        Long itemId,
        String sku,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal) {
}
