package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.SaleDto;
import com.ibm.grocery.contracts.SaleLineRequest;
import com.ibm.grocery.contracts.SaleRequest;
import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.exception.ResourceNotFoundException;
import com.ibm.grocery.core.mapper.SaleMapper;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.SaleRepository;
import com.ibm.grocery.domain.Item;
import com.ibm.grocery.domain.Sale;
import com.ibm.grocery.domain.SaleLine;
import com.ibm.grocery.domain.StockMovementType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Recording a sale and reducing stock happen in one transaction, so a failed line never leaves
 * partially decremented stock behind.
 */
@ApplicationScoped
public class SalesService {

    private static final int DEFAULT_LIST_LIMIT = 50;

    @Inject
    ItemRepository itemRepository;

    @Inject
    SaleRepository saleRepository;

    @Inject
    StockLedger stockLedger;

    @Transactional
    public SaleDto record(SaleRequest request, String soldBy) {
        Sale sale = new Sale();
        sale.setReference(nextReference());
        sale.setSoldBy(soldBy);

        Set<Long> seenItems = new HashSet<>();
        for (SaleLineRequest lineRequest : request.lines()) {
            if (!seenItems.add(lineRequest.itemId())) {
                throw new BusinessRuleException("Item " + lineRequest.itemId() + " appears more than once");
            }
            sale.addLine(buildLine(sale, lineRequest, soldBy));
        }

        return SaleMapper.toDto(saleRepository.save(sale));
    }

    public List<SaleDto> recent() {
        return saleRepository.findRecent(DEFAULT_LIST_LIMIT).stream().map(SaleMapper::toDto).toList();
    }

    public SaleDto get(Long id) {
        return saleRepository.findById(id)
                .map(SaleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
    }

    private SaleLine buildLine(Sale sale, SaleLineRequest lineRequest, String soldBy) {
        Item item = itemRepository.findByIdForUpdate(lineRequest.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", lineRequest.itemId()));

        stockLedger.apply(item, StockMovementType.SALE, lineRequest.quantity(),
                sale.getReference(), null, soldBy);

        SaleLine line = new SaleLine();
        line.setItem(item);
        line.setQuantity(lineRequest.quantity());
        line.setUnitPrice(item.getUnitPrice());
        line.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(lineRequest.quantity())));
        return line;
    }

    private String nextReference() {
        return "SALE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
