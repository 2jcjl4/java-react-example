package com.ibm.grocery.core.mapper;

import com.ibm.grocery.contracts.ItemDto;
import com.ibm.grocery.domain.Item;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getCategory(),
                item.getUnitPrice(),
                item.getQuantityOnHand(),
                item.getReorderLevel(),
                item.isActive(),
                item.isLowStock());
    }
}
