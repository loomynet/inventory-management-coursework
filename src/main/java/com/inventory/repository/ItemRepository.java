package com.inventory.repository;

import java.util.List;
import java.util.Optional;

import com.inventory.model.InventoryItem;

public interface ItemRepository {
    
    InventoryItem save(InventoryItem item);
    
    Optional<InventoryItem> findById(String id);
    
    List<InventoryItem> findAll();

    boolean delete(String id);
}
