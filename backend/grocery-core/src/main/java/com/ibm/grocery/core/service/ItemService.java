package com.ibm.grocery.core.service;

import com.ibm.grocery.contracts.ItemDto;
import com.ibm.grocery.contracts.ItemRequest;
import com.ibm.grocery.core.exception.BusinessRuleException;
import com.ibm.grocery.core.exception.ResourceNotFoundException;
import com.ibm.grocery.core.mapper.ItemMapper;
import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.domain.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ItemService {

    @Inject
    ItemRepository itemRepository;

    public List<ItemDto> search(String term, boolean includeInactive) {
        return itemRepository.search(term, includeInactive).stream().map(ItemMapper::toDto).toList();
    }

    public ItemDto get(Long id) {
        return ItemMapper.toDto(require(id));
    }

    @Transactional
    public ItemDto create(ItemRequest request) {
        itemRepository.findBySku(request.sku()).ifPresent(existing -> {
            throw new BusinessRuleException("An item with SKU " + request.sku() + " already exists");
        });

        Item item = new Item();
        applyRequest(item, request);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto update(Long id, ItemRequest request) {
        Item item = require(id);
        itemRepository.findBySku(request.sku())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new BusinessRuleException("An item with SKU " + request.sku() + " already exists");
                });

        applyRequest(item, request);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto setActive(Long id, boolean active) {
        Item item = require(id);
        item.setActive(active);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    private void applyRequest(Item item, ItemRequest request) {
        item.setSku(request.sku().trim());
        item.setName(request.name().trim());
        item.setCategory(request.category().trim());
        item.setUnitPrice(request.unitPrice());
        item.setReorderLevel(request.reorderLevel());
    }

    private Item require(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", id));
    }
}
