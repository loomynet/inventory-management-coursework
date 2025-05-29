package com.inventory.service;

public class Validator {

    public boolean validateId(String id) {
        return id != null && !id.trim().isEmpty() && id.matches("^[A-Za-z0-9-_]+$");
    }

    public boolean validateName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    public boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    public boolean validatePrice(double price) {
        return price >= 0.0;
    }
}
