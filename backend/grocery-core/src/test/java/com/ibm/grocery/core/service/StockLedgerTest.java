package com.ibm.grocery.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.StockMovementRepository;
import com.ibm.grocery.domain.Item;
import com.ibm.grocery.domain.StockMovement;
import com.ibm.grocery.domain.StockMovementType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockLedgerTest {

    private final List<StockMovement> recorded = new ArrayList<>();
    private StockLedger ledger;
    private Item item;

    @BeforeEach
    void setUp() {
        ledger = new StockLedger();
        ledger.itemRepository = new ItemRepository() {
            @Override
            public Item save(Item saved) {
                return saved;
            }
        };
        ledger.stockMovementRepository = new StockMovementRepository() {
            @Override
            public void record(StockMovement movement) {
                recorded.add(movement);
            }
        };

        item = new Item();
        item.setSku("GRO-001");
        item.setName("Whole Milk 2L");
        item.setCategory("Dairy");
        item.setUnitPrice(new BigDecimal("2.19"));
        item.setQuantityOnHand(10);
    }

    @Test
    void aSaleReducesStockAndRecordsTheMovement() {
        ledger.apply(item, StockMovementType.SALE, 4, "SALE-1", null, "cashier");

        assertEquals(6, item.getQuantityOnHand());
        assertEquals(1, recorded.size());
        assertEquals(-4, recorded.get(0).getQuantityDelta());
        assertEquals(6, recorded.get(0).getResultingQuantity());
    }

    @Test
    void aReceiptIncreasesStock() {
        ledger.apply(item, StockMovementType.RECEIPT, 15, null, "Delivery", "manager");

        assertEquals(25, item.getQuantityOnHand());
        assertEquals(15, recorded.get(0).getQuantityDelta());
    }

    @Test
    void stockCannotGoNegative() {
        assertThrows(BusinessRuleException.class,
                () -> ledger.apply(item, StockMovementType.SALE, 11, "SALE-2", null, "cashier"));

        assertEquals(10, item.getQuantityOnHand());
        assertEquals(0, recorded.size());
    }

    @Test
    void discontinuedItemsCannotMove() {
        item.setActive(false);

        assertThrows(BusinessRuleException.class,
                () -> ledger.apply(item, StockMovementType.RECEIPT, 1, null, null, "manager"));
    }
}
