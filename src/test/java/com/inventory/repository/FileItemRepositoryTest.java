package com.inventory.repository;

import com.inventory.model.InventoryItem;
import com.inventory.model.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileItemRepositoryTest {

    @TempDir
    Path tempDir;
    
    private FileHandler fileHandler;
    private FileItemRepository repository;
    private InventoryItem testItem;
    private ItemCategory testCategory;
    
    @BeforeEach
    void setUp() {
        String filePath = tempDir.resolve("test_inventory.json").toString();
        fileHandler = new FileHandler(filePath);
        repository = new FileItemRepository(fileHandler);
        
        testCategory = new ItemCategory(1, "Electronics", "Electronic devices");
        testItem = new InventoryItem("ITM001", "Laptop", "15-inch laptop", 10, 999.99, testCategory);
    }
    
    @Test
    void save_NewItem_ReturnsSavedItem() {
        // Act
        InventoryItem result = repository.save(testItem);
        
        // Assert
        assertEquals(testItem, result);
        
        // Verify item was saved
        Optional<InventoryItem> retrieved = repository.findById(testItem.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(testItem, retrieved.get());
    }
    
    @Test
    void save_ExistingItem_UpdatesItem() {
        // Arrange
        repository.save(testItem);
        
        // Modify the item
        testItem.setPrice(899.99);
        testItem.setQuantity(15);
        
        // Act
        InventoryItem result = repository.save(testItem);
        
        // Assert
        assertEquals(testItem, result);
        
        // Verify item was updated
        Optional<InventoryItem> retrieved = repository.findById(testItem.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(899.99, retrieved.get().getPrice());
        assertEquals(15, retrieved.get().getQuantity());
    }
    
    @Test
    void findById_ExistingId_ReturnsItem() {
        // Arrange
        repository.save(testItem);
        
        // Act
        Optional<InventoryItem> result = repository.findById(testItem.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(testItem, result.get());
    }
    
    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<InventoryItem> result = repository.findById("NONEXISTENT");
        
        // Assert
        assertFalse(result.isPresent());
    }
    
    @Test
    void findAll_ReturnsAllItems() {
        // Arrange
        repository.save(testItem);
        
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        repository.save(item2);
        
        // Act
        List<InventoryItem> result = repository.findAll();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(item -> item.getId().equals("ITM001")));
        assertTrue(result.stream().anyMatch(item -> item.getId().equals("ITM002")));
    }
    
    @Test
    void delete_ExistingId_ReturnsTrue() {
        // Arrange
        repository.save(testItem);
        
        // Act
        boolean result = repository.delete(testItem.getId());
        
        // Assert
        assertTrue(result);
        
        // Verify item was deleted
        Optional<InventoryItem> retrieved = repository.findById(testItem.getId());
        assertFalse(retrieved.isPresent());
    }
    
    @Test
    void delete_NonExistingId_ReturnsFalse() {
        // Act
        boolean result = repository.delete("NONEXISTENT");
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void persistence_SavedItemsAreLoadedAfterRepositoryRecreation() throws IOException {
        // Arrange
        String filePath = tempDir.resolve("persistence_test.json").toString();
        FileHandler handler = new FileHandler(filePath);
        
        // Create first repository and save items
        FileItemRepository repo1 = new FileItemRepository(handler);
        repo1.save(testItem);
        
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        repo1.save(item2);
        
        // Create second repository with same file handler
        FileItemRepository repo2 = new FileItemRepository(handler);
        
        // Act
        List<InventoryItem> result = repo2.findAll();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(item -> item.getId().equals("ITM001")));
        assertTrue(result.stream().anyMatch(item -> item.getId().equals("ITM002")));
    }
}
