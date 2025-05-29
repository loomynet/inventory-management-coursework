package com.inventory.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventory.model.InventoryData;
import com.inventory.model.InventoryItem;
import com.inventory.model.ItemCategory;

public class FileItemRepository implements ItemRepository {
    private final FileHandler fileHandler;
    private final Gson gson;
    private List<InventoryItem> items;
    private List<ItemCategory> categories;

    public FileItemRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    private void loadData() {
        try {
            if (!fileHandler.fileExists()) {
                fileHandler.createFile();
                this.items = new ArrayList<>();
                this.categories = new ArrayList<>();
                return;
            }

            String content = fileHandler.readFromFile();
            if (content == null || content.trim().isEmpty()) {
                this.items = new ArrayList<>();
                this.categories = new ArrayList<>();
                return;
            }

            InventoryData data = gson.fromJson(content, InventoryData.class);
            if (data == null) {
                this.items = new ArrayList<>();
                this.categories = new ArrayList<>();
            } else {
                this.items = data.getItems() != null ? data.getItems() : new ArrayList<>();
                this.categories = data.getCategories() != null ? data.getCategories() : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            this.items = new ArrayList<>();
            this.categories = new ArrayList<>();
        }
    }

    private void saveData() {
        try {
            InventoryData data = new InventoryData(items, categories);
            String json = gson.toJson(data);
            fileHandler.writeToFile(json);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (item.getCategory() != null) {
            saveCategory(item.getCategory());
        }

        Optional<InventoryItem> existingItem = findById(item.getId());
        
        if (existingItem.isPresent()) {
            items = items.stream()
                    .map(i -> i.getId().equals(item.getId()) ? item : i)
                    .collect(Collectors.toList());
        } else {
            items.add(item);
        }
        
        saveData();
        return item;
    }

    private ItemCategory saveCategory(ItemCategory category) {
        Optional<ItemCategory> existingCategory = categories.stream()
                .filter(c -> c.getId() == category.getId())
                .findFirst();
        
        if (existingCategory.isPresent()) {
            categories = categories.stream()
                    .map(c -> c.getId() == category.getId() ? category : c)
                    .collect(Collectors.toList());
        } else {
            categories.add(category);
        }
        
        return category;
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean delete(String id) {
        if (id == null) {
            return false;
        }
        
        int initialSize = items.size();
        items = items.stream()
                .filter(item -> !item.getId().equals(id))
                .collect(Collectors.toList());
        
        boolean deleted = items.size() < initialSize;
        if (deleted) {
            saveData();
        }
        
        return deleted;
    }
}
