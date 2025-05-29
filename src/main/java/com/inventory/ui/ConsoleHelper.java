package com.inventory.ui;

import java.util.List;
import java.util.Scanner;

import com.inventory.model.InventoryItem;

public class ConsoleHelper {
    private final Scanner scanner;

    public ConsoleHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String readInput() {
        return scanner.nextLine().trim();
    }

    public void printError(String error) {
        System.err.println("ERROR: " + error);
    }

    public void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public void printTable(List<InventoryItem> items) {
        String header = String.format("%-10s %-20s %-30s %-10s %-10s %-20s",
                "ID", "Name", "Description", "Quantity", "Price", "Category");
        printMessage(header);
        printMessage("-".repeat(100));

        for (InventoryItem item : items) {
            String row = String.format("%-10s %-20s %-30s %-10d $%-9.2f %-20s",
                    item.getId(),
                    truncate(item.getName(), 20),
                    truncate(item.getDescription(), 30),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getCategory() != null ? item.getCategory().getName() : "N/A");
            printMessage(row);
        }
    }

    public void waitForEnter() {
        printMessage("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private String truncate(String str, int length) {
        if (str == null) {
            return "";
        }
        
        if (str.length() <= length) {
            return str;
        }
        
        return str.substring(0, length - 3) + "...";
    }
}
