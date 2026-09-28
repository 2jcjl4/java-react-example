package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.StockAdjustmentRequest;
import com.ibm.grocery.contracts.StockMovementDto;
import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.exception.ResourceNotFoundException;
import com.ibm.grocery.core.mapper.StockMovementMapper;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.StockMovementRepository;
import com.ibm.grocery.domain.Item;
import com.ibm.grocery.domain.StockMovement;
import com.ibm.grocery.domain.StockMovementType;
import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Singleton
public class StockService {

    private static final int DEFAULT_HISTORY_LIMIT = 100;

    @Inject
    ItemRepository itemRepository;

    @Inject
    StockMovementRepository stockMovementRepository;

    @Inject
    StockLedger stockLedger;

    @Transactional
    public StockMovementDto adjust(Long itemId, StockAdjustmentRequest request, String performedBy) {
        StockMovementType movementType = parseMovementType(request.movementType());
        if (movementType == StockMovementType.SALE) {
            throw new BusinessRuleException("Sales must be recorded through the sales endpoint");
        }

        Item item = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", itemId));

        StockMovement movement = stockLedger.apply(
                item, movementType, request.quantity(), null, request.note(), performedBy);
        return StockMovementMapper.toDto(movement);
    }

    public List<StockMovementDto> history(Long itemId) {
        return stockMovementRepository.findRecent(itemId, DEFAULT_HISTORY_LIMIT).stream()
                .map(StockMovementMapper::toDto)
                .toList();
    }

    private StockMovementType parseMovementType(String value) {
        try {
            return StockMovementType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BusinessRuleException("Unknown stock movement type: " + value);
        }
    }
}
