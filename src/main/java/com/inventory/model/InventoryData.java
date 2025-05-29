package com.inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryData {
    private List<InventoryItem> items;
    private List<ItemCategory> categories;

    public InventoryData() {
        this.items = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public InventoryData(List<InventoryItem> items, List<ItemCategory> categories) {
        this.items = items;
        this.categories = categories;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    public List<ItemCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ItemCategory> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryData that = (InventoryData) o;
        return Objects.equals(items, that.items) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, categories);
    }

    @Override
    public String toString() {
        return "InventoryData{" +
                "items=" + items +
                ", categories=" + categories +
                '}';
    }
}
