package com.ibm.grocery.core.mapper;

import com.ibm.grocery.contracts.SaleDto;
import com.ibm.grocery.contracts.SaleLineDto;
import com.ibm.grocery.domain.Sale;
import com.ibm.grocery.domain.SaleLine;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class SaleMapper {

    private SaleMapper() {
    }

    public static SaleDto toDto(Sale sale) {
        List<SaleLineDto> lines = sale.getLines().stream().map(SaleMapper::toLineDto).toList();
        return new SaleDto(
                sale.getId(),
                sale.getReference(),
                sale.getSoldBy(),
                DateTimeFormatter.ISO_INSTANT.format(sale.getSoldAt()),
                sale.getTotalAmount(),
                lines);
    }

    private static SaleLineDto toLineDto(SaleLine line) {
        return new SaleLineDto(
                line.getItem().getId(),
                line.getItem().getSku(),
                line.getItem().getName(),
                line.getQuantity(),
                line.getUnitPrice(),
                line.getLineTotal());
    }
}
