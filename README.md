# Java CLI Inventory Management System

A command-line interface application for inventory management, developed in Java using object-oriented programming principles.

## Description

This Inventory Management System allows users to manage inventory items through a text-based interface. The application provides functionality for adding, updating, deleting, searching, and sorting inventory items, with data persistence between sessions using JSON file storage.

## Features

- Add new inventory items with details (ID, name, description, quantity, price, category)
- View all items in a formatted table
- Search for items by keyword
- Update existing items
- Delete items from inventory
- Sort items by different criteria (name, price, quantity)
- Data persistence using JSON file storage

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone this repository:
   ```
   git clone https://github.com/loomynet/inventory-management-coursework.git
   cd inventory-management-coursework
   ```

2. Build the project using Maven:
   ```
   mvn clean package
   ```

## Running the Application

After building, you can run the application using one of these methods:

1. Using the JAR file:
   ```
   java -jar target/inventory-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

2. Using Maven:
   ```
   mvn exec:java -Dexec.mainClass="com.inventory.ui.InventoryApp"
   ```

## Usage Guide

Once the application is running, you'll see a main menu with the following options:

```
===== Inventory Management System =====
1. Add new item
2. View all items
3. Search items
4. Update item
5. Delete item
6. Sort items
0. Exit
=======================================
Enter your choice:
```

### Adding an Item
- Select option 1
- Enter the requested details (ID, name, description, quantity, price)
- Enter category details (ID, name, description)

### Viewing Items
- Select option 2 to see all items displayed in a table format

### Searching for Items
- Select option 3
- Enter a keyword to search in item names, descriptions, or categories

### Updating an Item
- Select option 4
- Enter the ID of the item to update
- Enter new values or press Enter to keep current values

### Deleting an Item
- Select option 5
- Enter the ID of the item to delete
- Confirm deletion when prompted

### Sorting Items
- Select option 6
- Choose sort criteria (name, price, quantity)
- Choose sort order (ascending or descending)

### Exiting the Application
- Select option 0 to exit

## Data Storage

The application stores data in a file named `inventory_data.json` in the directory from which the application is run. This file is automatically created if it doesn't exist.

## Project Structure

- `model`: Contains data structures (InventoryItem, ItemCategory)
- `repository`: Handles data persistence (ItemRepository, FileItemRepository)
- `service`: Contains business logic (InventoryService, Validator)
- `ui`: Handles user interaction (InventoryApp, MenuManager, ConsoleHelper)

## Testing

Run the tests using Maven:
```
mvn test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
