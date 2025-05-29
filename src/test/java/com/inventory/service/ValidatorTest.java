package com.inventory.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private final Validator validator = new Validator();

    @Test
    void validateId_ValidId_ReturnsTrue() {
        // Test valid IDs
        assertTrue(validator.validateId("ITM001"));
        assertTrue(validator.validateId("ITEM_123"));
        assertTrue(validator.validateId("item-456"));
        assertTrue(validator.validateId("A1B2C3"));
    }

    @Test
    void validateId_InvalidId_ReturnsFalse() {
        // Test null and empty
        assertFalse(validator.validateId(null));
        assertFalse(validator.validateId(""));
        assertFalse(validator.validateId("   "));
        
        // Test invalid characters
        assertFalse(validator.validateId("ITM 001")); // space
        assertFalse(validator.validateId("ITM#001")); // special character
        assertFalse(validator.validateId("ITM@001")); // special character
    }

    @Test
    void validateName_ValidName_ReturnsTrue() {
        // Test valid names
        assertTrue(validator.validateName("Laptop"));
        assertTrue(validator.validateName("15-inch Laptop"));
        assertTrue(validator.validateName("Phone with 128GB storage"));
        
        // Test name with exactly 100 characters
        String nameWith100Chars = "a".repeat(100);
        assertTrue(validator.validateName(nameWith100Chars));
    }

    @Test
    void validateName_InvalidName_ReturnsFalse() {
        // Test null and empty
        assertFalse(validator.validateName(null));
        assertFalse(validator.validateName(""));
        assertFalse(validator.validateName("   "));
        
        // Test name with more than 100 characters
        String nameWith101Chars = "a".repeat(101);
        assertFalse(validator.validateName(nameWith101Chars));
    }

    @Test
    void validateQuantity_ValidQuantity_ReturnsTrue() {
        // Test valid quantities
        assertTrue(validator.validateQuantity(0));
        assertTrue(validator.validateQuantity(1));
        assertTrue(validator.validateQuantity(100));
        assertTrue(validator.validateQuantity(Integer.MAX_VALUE));
    }

    @Test
    void validateQuantity_InvalidQuantity_ReturnsFalse() {
        // Test negative quantities
        assertFalse(validator.validateQuantity(-1));
        assertFalse(validator.validateQuantity(-100));
        assertFalse(validator.validateQuantity(Integer.MIN_VALUE));
    }

    @Test
    void validatePrice_ValidPrice_ReturnsTrue() {
        // Test valid prices
        assertTrue(validator.validatePrice(0.0));
        assertTrue(validator.validatePrice(0.01));
        assertTrue(validator.validatePrice(99.99));
        assertTrue(validator.validatePrice(1000.0));
        assertTrue(validator.validatePrice(Double.MAX_VALUE));
    }

    @Test
    void validatePrice_InvalidPrice_ReturnsFalse() {
        // Test negative prices
        assertFalse(validator.validatePrice(-0.01));
        assertFalse(validator.validatePrice(-99.99));
        assertFalse(validator.validatePrice(-1000.0));
        assertFalse(validator.validatePrice(Double.NEGATIVE_INFINITY));
    }
}
