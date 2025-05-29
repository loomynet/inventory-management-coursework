package com.inventory.service;

import com.inventory.model.InventoryItem;
import com.inventory.model.ItemCategory;
import com.inventory.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private Validator validator;

    private InventoryService inventoryService;
    private InventoryItem testItem;
    private ItemCategory testCategory;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(itemRepository, validator);
        
        testCategory = new ItemCategory(1, "Electronics", "Electronic devices");
        testItem = new InventoryItem("ITM001", "Laptop", "15-inch laptop", 10, 999.99, testCategory);
    }

    @Test
    void addItem_ValidItem_ReturnsAddedItem() {
        // Arrange
        when(validator.validateId(testItem.getId())).thenReturn(true);
        when(validator.validateName(testItem.getName())).thenReturn(true);
        when(validator.validateQuantity(testItem.getQuantity())).thenReturn(true);
        when(validator.validatePrice(testItem.getPrice())).thenReturn(true);
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.empty());
        when(itemRepository.save(testItem)).thenReturn(testItem);

        // Act
        InventoryItem result = inventoryService.addItem(testItem);

        // Assert
        assertEquals(testItem, result);
        verify(itemRepository).findById(testItem.getId());
        verify(itemRepository).save(testItem);
    }

    @Test
    void addItem_ExistingId_ThrowsException() {
        // Arrange
        when(validator.validateId(testItem.getId())).thenReturn(true);
        when(validator.validateName(testItem.getName())).thenReturn(true);
        when(validator.validateQuantity(testItem.getQuantity())).thenReturn(true);
        when(validator.validatePrice(testItem.getPrice())).thenReturn(true);
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.addItem(testItem);
        });
        
        assertTrue(exception.getMessage().contains("already exists"));
        verify(itemRepository).findById(testItem.getId());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem_ValidItem_ReturnsUpdatedItem() {
        // Arrange
        when(validator.validateId(testItem.getId())).thenReturn(true);
        when(validator.validateName(testItem.getName())).thenReturn(true);
        when(validator.validateQuantity(testItem.getQuantity())).thenReturn(true);
        when(validator.validatePrice(testItem.getPrice())).thenReturn(true);
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(itemRepository.save(testItem)).thenReturn(testItem);

        // Act
        InventoryItem result = inventoryService.updateItem(testItem);

        // Assert
        assertEquals(testItem, result);
        verify(itemRepository).findById(testItem.getId());
        verify(itemRepository).save(testItem);
    }

    @Test
    void updateItem_NonExistingId_ThrowsException() {
        // Arrange
        when(validator.validateId(testItem.getId())).thenReturn(true);
        when(validator.validateName(testItem.getName())).thenReturn(true);
        when(validator.validateQuantity(testItem.getQuantity())).thenReturn(true);
        when(validator.validatePrice(testItem.getPrice())).thenReturn(true);
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.updateItem(testItem);
        });
        
        assertTrue(exception.getMessage().contains("not found"));
        verify(itemRepository).findById(testItem.getId());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void deleteItem_ExistingId_ReturnsTrue() {
        // Arrange
        String id = "ITM001";
        when(validator.validateId(id)).thenReturn(true);
        when(itemRepository.delete(id)).thenReturn(true);

        // Act
        boolean result = inventoryService.deleteItem(id);

        // Assert
        assertTrue(result);
        verify(itemRepository).delete(id);
    }

    @Test
    void findItemById_ExistingId_ReturnsItem() {
        // Arrange
        String id = "ITM001";
        when(validator.validateId(id)).thenReturn(true);
        when(itemRepository.findById(id)).thenReturn(Optional.of(testItem));

        // Act
        Optional<InventoryItem> result = inventoryService.findItemById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testItem, result.get());
        verify(itemRepository).findById(id);
    }

    @Test
    void getAllItems_ReturnsAllItems() {
        // Arrange
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        List<InventoryItem> items = Arrays.asList(testItem, item2);
        when(itemRepository.findAll()).thenReturn(items);

        // Act
        List<InventoryItem> result = inventoryService.getAllItems();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(testItem));
        assertTrue(result.contains(item2));
        verify(itemRepository).findAll();
    }

    @Test
    void searchItems_MatchingKeyword_ReturnsMatchingItems() {
        // Arrange
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        List<InventoryItem> allItems = Arrays.asList(testItem, item2);
        when(itemRepository.findAll()).thenReturn(allItems);

        // Act
        List<InventoryItem> result = inventoryService.searchItems("laptop");

        // Assert
        assertEquals(1, result.size());
        assertEquals(testItem, result.get(0));
        verify(itemRepository).findAll();
    }

    @Test
    void sortItems_ByName_ReturnsSortedItems() {
        // Arrange
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        List<InventoryItem> allItems = Arrays.asList(testItem, item2);
        when(itemRepository.findAll()).thenReturn(allItems);

        // Act
        List<InventoryItem> result = inventoryService.sortItems("name", true);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName()); // L comes before P
        assertEquals("Phone", result.get(1).getName());
        verify(itemRepository).findAll();
    }

    @Test
    void sortItems_ByPrice_ReturnsSortedItems() {
        // Arrange
        InventoryItem item2 = new InventoryItem("ITM002", "Phone", "Smartphone", 20, 499.99, testCategory);
        List<InventoryItem> allItems = Arrays.asList(testItem, item2);
        when(itemRepository.findAll()).thenReturn(allItems);

        // Act
        List<InventoryItem> result = inventoryService.sortItems("price", true);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Phone", result.get(0).getName()); // Lower price first
        assertEquals("Laptop", result.get(1).getName());
        verify(itemRepository).findAll();
    }
}
