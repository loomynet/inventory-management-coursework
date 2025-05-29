package com.inventory.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.inventory.model.InventoryItem;
import com.inventory.model.ItemCategory;
import com.inventory.service.InventoryService;

public class MenuManager {
    private final Scanner scanner;
    private final InventoryService inventoryService;
    private final ConsoleHelper consoleHelper;

    public MenuManager(Scanner scanner, InventoryService inventoryService, ConsoleHelper consoleHelper) {
        this.scanner = scanner;
        this.inventoryService = inventoryService;
        this.consoleHelper = consoleHelper;
    }

    public void displayMainMenu() {
        boolean exit = false;
        
        while (!exit) {
            consoleHelper.clearScreen();
            consoleHelper.printMessage("===== Inventory Management System =====");
            consoleHelper.printMessage("1. Add new item");
            consoleHelper.printMessage("2. View all items");
            consoleHelper.printMessage("3. Search items");
            consoleHelper.printMessage("4. Update item");
            consoleHelper.printMessage("5. Delete item");
            consoleHelper.printMessage("6. Sort items");
            consoleHelper.printMessage("0. Exit");
            consoleHelper.printMessage("=======================================");
            consoleHelper.printMessage("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                exit = handleUserInput(choice);
            } catch (NumberFormatException e) {
                consoleHelper.printError("Invalid input. Please enter a number.");
                consoleHelper.waitForEnter();
            }
        }
    }

    private boolean handleUserInput(int choice) {
        switch (choice) {
            case 1:
                addItemMenu();
                return false;
            case 2:
                viewAllItemsMenu();
                return false;
            case 3:
                searchItemsMenu();
                return false;
            case 4:
                updateItemMenu();
                return false;
            case 5:
                deleteItemMenu();
                return false;
            case 6:
                sortItemsMenu();
                return false;
            case 0:
                consoleHelper.printMessage("Exiting application. Goodbye!");
                return true;
            default:
                consoleHelper.printError("Invalid choice. Please try again.");
                consoleHelper.waitForEnter();
                return false;
        }
    }

    private void addItemMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== Add New Item =====");
        
        try {
            consoleHelper.printMessage("Enter item ID: ");
            String id = scanner.nextLine().trim();
            
            consoleHelper.printMessage("Enter item name: ");
            String name = scanner.nextLine().trim();
            
            consoleHelper.printMessage("Enter item description: ");
            String description = scanner.nextLine().trim();
            
            consoleHelper.printMessage("Enter item quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            
            consoleHelper.printMessage("Enter item price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            consoleHelper.printMessage("Enter category ID: ");
            int categoryId = Integer.parseInt(scanner.nextLine().trim());
            
            consoleHelper.printMessage("Enter category name: ");
            String categoryName = scanner.nextLine().trim();
            
            consoleHelper.printMessage("Enter category description: ");
            String categoryDescription = scanner.nextLine().trim();
            
            ItemCategory category = new ItemCategory(categoryId, categoryName, categoryDescription);

            InventoryItem item = new InventoryItem(id, name, description, quantity, price, category);
            inventoryService.addItem(item);
            
            consoleHelper.printMessage("Item added successfully!");
        } catch (NumberFormatException e) {
            consoleHelper.printError("Invalid number format. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            consoleHelper.printError("Error: " + e.getMessage());
        }
        
        consoleHelper.waitForEnter();
    }

    private void viewAllItemsMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== All Items =====");
        
        List<InventoryItem> items = inventoryService.getAllItems();
        
        if (items.isEmpty()) {
            consoleHelper.printMessage("No items found in inventory.");
        } else {
            consoleHelper.printTable(items);
        }
        
        consoleHelper.waitForEnter();
    }

    private void searchItemsMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== Search Items =====");
        
        consoleHelper.printMessage("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();
        
        List<InventoryItem> items = inventoryService.searchItems(keyword);
        
        if (items.isEmpty()) {
            consoleHelper.printMessage("No items found matching '" + keyword + "'.");
        } else {
            consoleHelper.printMessage("Search results for '" + keyword + "':");
            consoleHelper.printTable(items);
        }
        
        consoleHelper.waitForEnter();
    }

    private void updateItemMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== Update Item =====");
        
        consoleHelper.printMessage("Enter item ID to update: ");
        String id = scanner.nextLine().trim();
        
        try {
            Optional<InventoryItem> optionalItem = inventoryService.findItemById(id);
            
            if (optionalItem.isPresent()) {
                InventoryItem item = optionalItem.get();
                consoleHelper.printMessage("Current item details:");
                consoleHelper.printMessage(item.toString());

                consoleHelper.printMessage("\nEnter new item name (or press Enter to keep current): ");
                String name = scanner.nextLine().trim();
                if (!name.isEmpty()) {
                    item.setName(name);
                }
                
                consoleHelper.printMessage("Enter new item description (or press Enter to keep current): ");
                String description = scanner.nextLine().trim();
                if (!description.isEmpty()) {
                    item.setDescription(description);
                }
                
                consoleHelper.printMessage("Enter new item quantity (or press Enter to keep current): ");
                String quantityStr = scanner.nextLine().trim();
                if (!quantityStr.isEmpty()) {
                    item.setQuantity(Integer.parseInt(quantityStr));
                }
                
                consoleHelper.printMessage("Enter new item price (or press Enter to keep current): ");
                String priceStr = scanner.nextLine().trim();
                if (!priceStr.isEmpty()) {
                    item.setPrice(Double.parseDouble(priceStr));
                }

                consoleHelper.printMessage("Update category? (y/n): ");
                String updateCategory = scanner.nextLine().trim().toLowerCase();
                
                if (updateCategory.equals("y")) {
                    consoleHelper.printMessage("Enter new category ID: ");
                    int categoryId = Integer.parseInt(scanner.nextLine().trim());
                    
                    consoleHelper.printMessage("Enter new category name: ");
                    String categoryName = scanner.nextLine().trim();
                    
                    consoleHelper.printMessage("Enter new category description: ");
                    String categoryDescription = scanner.nextLine().trim();
                    
                    ItemCategory category = new ItemCategory(categoryId, categoryName, categoryDescription);
                    item.setCategory(category);
                }

                inventoryService.updateItem(item);
                consoleHelper.printMessage("Item updated successfully!");
            } else {
                consoleHelper.printError("Item with ID '" + id + "' not found.");
            }
        } catch (NumberFormatException e) {
            consoleHelper.printError("Invalid number format. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            consoleHelper.printError("Error: " + e.getMessage());
        }
        
        consoleHelper.waitForEnter();
    }

    private void deleteItemMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== Delete Item =====");
        
        consoleHelper.printMessage("Enter item ID to delete: ");
        String id = scanner.nextLine().trim();
        
        try {
            Optional<InventoryItem> optionalItem = inventoryService.findItemById(id);
            
            if (optionalItem.isPresent()) {
                InventoryItem item = optionalItem.get();
                consoleHelper.printMessage("Item to delete:");
                consoleHelper.printMessage(item.toString());
                
                consoleHelper.printMessage("\nAre you sure you want to delete this item? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y")) {
                    boolean deleted = inventoryService.deleteItem(id);
                    if (deleted) {
                        consoleHelper.printMessage("Item deleted successfully!");
                    } else {
                        consoleHelper.printError("Failed to delete item.");
                    }
                } else {
                    consoleHelper.printMessage("Deletion cancelled.");
                }
            } else {
                consoleHelper.printError("Item with ID '" + id + "' not found.");
            }
        } catch (IllegalArgumentException e) {
            consoleHelper.printError("Error: " + e.getMessage());
        }
        
        consoleHelper.waitForEnter();
    }

    private void sortItemsMenu() {
        consoleHelper.clearScreen();
        consoleHelper.printMessage("===== Sort Items =====");
        
        consoleHelper.printMessage("Sort by:");
        consoleHelper.printMessage("1. Name");
        consoleHelper.printMessage("2. Price");
        consoleHelper.printMessage("3. Quantity");
        consoleHelper.printMessage("Enter your choice: ");
        
        try {
            int sortChoice = Integer.parseInt(scanner.nextLine().trim());
            String sortBy;
            
            switch (sortChoice) {
                case 1:
                    sortBy = "name";
                    break;
                case 2:
                    sortBy = "price";
                    break;
                case 3:
                    sortBy = "quantity";
                    break;
                default:
                    consoleHelper.printError("Invalid choice. Sorting by name.");
                    sortBy = "name";
            }
            
            consoleHelper.printMessage("Sort order:");
            consoleHelper.printMessage("1. Ascending");
            consoleHelper.printMessage("2. Descending");
            consoleHelper.printMessage("Enter your choice: ");
            
            int orderChoice = Integer.parseInt(scanner.nextLine().trim());
            boolean ascending = orderChoice != 2;
            
            List<InventoryItem> sortedItems = inventoryService.sortItems(sortBy, ascending);
            
            consoleHelper.printMessage("Sorted items by " + sortBy + " in " + 
                    (ascending ? "ascending" : "descending") + " order:");
            consoleHelper.printTable(sortedItems);
        } catch (NumberFormatException e) {
            consoleHelper.printError("Invalid input. Please enter a number.");
        }
        
        consoleHelper.waitForEnter();
    }
}
