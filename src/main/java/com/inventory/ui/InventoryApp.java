package com.inventory.ui;

import java.util.Scanner;

import com.inventory.repository.FileHandler;
import com.inventory.repository.FileItemRepository;
import com.inventory.repository.ItemRepository;
import com.inventory.service.InventoryService;
import com.inventory.service.Validator;

public class InventoryApp {
    
    private static final String DATA_FILE_PATH = "inventory_data.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandler = new FileHandler(DATA_FILE_PATH);
        ItemRepository itemRepository = new FileItemRepository(fileHandler);
        Validator validator = new Validator();
        InventoryService inventoryService = new InventoryService(itemRepository, validator);
        ConsoleHelper consoleHelper = new ConsoleHelper(scanner);
        MenuManager menuManager = new MenuManager(scanner, inventoryService, consoleHelper);

        consoleHelper.clearScreen();
        consoleHelper.printMessage("Welcome to the Inventory Management System!");
        consoleHelper.printMessage("This application allows you to manage your inventory items.");
        consoleHelper.printMessage("You can add, view, search, update, and delete items.");
        consoleHelper.waitForEnter();

        menuManager.displayMainMenu();

        scanner.close();
    }
}
