package com.ibm.grocery.core.service;

import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.StockMovementRepository;
import com.ibm.grocery.domain.Item;
import com.ibm.grocery.domain.StockMovement;
import com.ibm.grocery.domain.StockMovementType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Single point where item quantities change. Every caller goes through here so the quantity on hand
 * and the movement audit trail can never drift apart.
 */
@ApplicationScoped
public class StockLedger {

    @Inject
    ItemRepository itemRepository;

    @Inject
    StockMovementRepository stockMovementRepository;

    public StockMovement apply(Item item,
                               StockMovementType movementType,
                               int quantity,
                               String reference,
                               String note,
                               String performedBy) {
        if (quantity <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }
        if (!item.isActive()) {
            throw new BusinessRuleException("Item " + item.getSku() + " is discontinued");
        }

        int delta = quantity * movementType.direction();
        int resulting = item.getQuantityOnHand() + delta;
        if (resulting < 0) {
            throw new BusinessRuleException("Insufficient stock for " + item.getSku()
                    + ". Requested " + quantity + ", available " + item.getQuantityOnHand());
        }

        item.applyQuantityChange(delta);
        itemRepository.save(item);

        StockMovement movement = new StockMovement();
        movement.setItem(item);
        movement.setMovementType(movementType);
        movement.setQuantityDelta(delta);
        movement.setResultingQuantity(resulting);
        movement.setReference(reference);
        movement.setNote(note);
        movement.setPerformedBy(performedBy);
        stockMovementRepository.record(movement);
        return movement;
    }
}
