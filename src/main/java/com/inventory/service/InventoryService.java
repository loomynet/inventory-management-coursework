package com.inventory.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.inventory.model.InventoryItem;
import com.inventory.repository.ItemRepository;

public class InventoryService {
    private final ItemRepository itemRepository;
    private final Validator validator;

    public InventoryService(ItemRepository itemRepository, Validator validator) {
        this.itemRepository = itemRepository;
        this.validator = validator;
    }

    public InventoryItem addItem(InventoryItem item) {
        validateItem(item);

        if (itemRepository.findById(item.getId()).isPresent()) {
            throw new IllegalArgumentException("Item with ID " + item.getId() + " already exists");
        }
        
        return itemRepository.save(item);
    }

    public InventoryItem updateItem(InventoryItem item) {
        validateItem(item);

        if (itemRepository.findById(item.getId()).isEmpty()) {
            throw new IllegalArgumentException("Item with ID " + item.getId() + " not found");
        }
        
        return itemRepository.save(item);
    }

    public boolean deleteItem(String id) {
        if (!validator.validateId(id)) {
            throw new IllegalArgumentException("Invalid item ID");
        }
        
        return itemRepository.delete(id);
    }

    public Optional<InventoryItem> findItemById(String id) {
        if (!validator.validateId(id)) {
            throw new IllegalArgumentException("Invalid item ID");
        }
        
        return itemRepository.findById(id);
    }

    public List<InventoryItem> getAllItems() {
        return itemRepository.findAll();
    }

    public List<InventoryItem> searchItems(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllItems();
        }
        
        String searchTerm = keyword.toLowerCase();
        return itemRepository.findAll().stream()
                .filter(item -> 
                    item.getName().toLowerCase().contains(searchTerm) || 
                    item.getDescription().toLowerCase().contains(searchTerm) ||
                    (item.getCategory() != null && 
                     item.getCategory().getName().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }

    public List<InventoryItem> sortItems(String sortBy, boolean ascending) {
        Comparator<InventoryItem> comparator;
        
        switch (sortBy.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(InventoryItem::getName);
                break;
            case "price":
                comparator = Comparator.comparing(InventoryItem::getPrice);
                break;
            case "quantity":
                comparator = Comparator.comparing(InventoryItem::getQuantity);
                break;
            default:
                comparator = Comparator.comparing(InventoryItem::getId);
        }
        
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        return itemRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private void validateItem(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        
        if (!validator.validateId(item.getId())) {
            throw new IllegalArgumentException("Invalid item ID");
        }
        
        if (!validator.validateName(item.getName())) {
            throw new IllegalArgumentException("Invalid item name");
        }
        
        if (!validator.validateQuantity(item.getQuantity())) {
            throw new IllegalArgumentException("Invalid item quantity");
        }
        
        if (!validator.validatePrice(item.getPrice())) {
            throw new IllegalArgumentException("Invalid item price");
        }
        
        if (item.getCategory() == null) {
            throw new IllegalArgumentException("Item category cannot be null");
        }
    }
}
