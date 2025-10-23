package com.example.jwtapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void defaultConstructor_CreatesProductWithTimestamps() {
        // Act
        Product newProduct = new Product();

        // Assert
        assertNotNull(newProduct);
        assertNull(newProduct.getId());
        assertNull(newProduct.getName());
        assertNull(newProduct.getDescription());
        assertNull(newProduct.getPrice());
        assertNull(newProduct.getStock());
        assertNotNull(newProduct.getCreatedAt());
        assertNotNull(newProduct.getUpdatedAt());
        assertTrue(newProduct.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newProduct.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void parameterizedConstructor_CreatesProductWithValues() {
        // Arrange
        String name = "Laptop";
        String description = "Gaming laptop";
        BigDecimal price = new BigDecimal("999.99");
        Integer stock = 10;

        // Act
        Product newProduct = new Product(name, description, price, stock);

        // Assert
        assertNotNull(newProduct);
        assertEquals(name, newProduct.getName());
        assertEquals(description, newProduct.getDescription());
        assertEquals(price, newProduct.getPrice());
        assertEquals(stock, newProduct.getStock());
        assertNotNull(newProduct.getCreatedAt());
        assertNotNull(newProduct.getUpdatedAt());
    }

    @Test
    void setId_ValidValue_SetsId() {
        // Act
        product.setId(1L);

        // Assert
        assertEquals(1L, product.getId());
    }

    @Test
    void setId_NullValue_SetsNull() {
        // Act
        product.setId(null);

        // Assert
        assertNull(product.getId());
    }

    @Test
    void getId_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        Long expectedId = 123L;
        product.setId(expectedId);

        // Act
        Long result = product.getId();

        // Assert
        assertEquals(expectedId, result);
    }

    @Test
    void setName_ValidValue_SetsName() {
        // Act
        product.setName("Laptop");

        // Assert
        assertEquals("Laptop", product.getName());
    }

    @Test
    void setName_NullValue_SetsNull() {
        // Act
        product.setName(null);

        // Assert
        assertNull(product.getName());
    }

    @Test
    void setName_EmptyValue_SetsEmpty() {
        // Act
        product.setName("");

        // Assert
        assertEquals("", product.getName());
    }

    @Test
    void getName_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedName = "Gaming Laptop";
        product.setName(expectedName);

        // Act
        String result = product.getName();

        // Assert
        assertEquals(expectedName, result);
    }

    @Test
    void setDescription_ValidValue_SetsDescription() {
        // Act
        product.setDescription("High-performance gaming laptop");

        // Assert
        assertEquals("High-performance gaming laptop", product.getDescription());
    }

    @Test
    void setDescription_NullValue_SetsNull() {
        // Act
        product.setDescription(null);

        // Assert
        assertNull(product.getDescription());
    }

    @Test
    void setDescription_EmptyValue_SetsEmpty() {
        // Act
        product.setDescription("");

        // Assert
        assertEquals("", product.getDescription());
    }

    @Test
    void getDescription_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedDescription = "Wireless mouse with RGB lighting";
        product.setDescription(expectedDescription);

        // Act
        String result = product.getDescription();

        // Assert
        assertEquals(expectedDescription, result);
    }

    @Test
    void setPrice_ValidValue_SetsPrice() {
        // Act
        BigDecimal price = new BigDecimal("299.99");
        product.setPrice(price);

        // Assert
        assertEquals(price, product.getPrice());
    }

    @Test
    void setPrice_NullValue_SetsNull() {
        // Act
        product.setPrice(null);

        // Assert
        assertNull(product.getPrice());
    }

    @Test
    void setPrice_ZeroValue_SetsZero() {
        // Act
        BigDecimal price = BigDecimal.ZERO;
        product.setPrice(price);

        // Assert
        assertEquals(price, product.getPrice());
    }

    @Test
    void getPrice_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        BigDecimal expectedPrice = new BigDecimal("1499.99");
        product.setPrice(expectedPrice);

        // Act
        BigDecimal result = product.getPrice();

        // Assert
        assertEquals(expectedPrice, result);
    }

    @Test
    void setStock_ValidValue_SetsStock() {
        // Act
        product.setStock(50);

        // Assert
        assertEquals(50, product.getStock());
    }

    @Test
    void setStock_NullValue_SetsNull() {
        // Act
        product.setStock(null);

        // Assert
        assertNull(product.getStock());
    }

    @Test
    void setStock_ZeroValue_SetsZero() {
        // Act
        product.setStock(0);

        // Assert
        assertEquals(0, product.getStock());
    }

    @Test
    void setStock_NegativeValue_SetsNegative() {
        // Act
        product.setStock(-5);

        // Assert
        assertEquals(-5, product.getStock());
    }

    @Test
    void getStock_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        Integer expectedStock = 100;
        product.setStock(expectedStock);

        // Act
        Integer result = product.getStock();

        // Assert
        assertEquals(expectedStock, result);
    }

    @Test
    void setCreatedAt_ValidValue_SetsCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);

        // Act
        product.setCreatedAt(createdAt);

        // Assert
        assertEquals(createdAt, product.getCreatedAt());
    }

    @Test
    void setCreatedAt_NullValue_SetsNull() {
        // Act
        product.setCreatedAt(null);

        // Assert
        assertNull(product.getCreatedAt());
    }

    @Test
    void getCreatedAt_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        LocalDateTime expectedCreatedAt = LocalDateTime.now().minusHours(2);
        product.setCreatedAt(expectedCreatedAt);

        // Act
        LocalDateTime result = product.getCreatedAt();

        // Assert
        assertEquals(expectedCreatedAt, result);
    }

    @Test
    void setUpdatedAt_ValidValue_SetsUpdatedAt() {
        // Arrange
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        product.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(updatedAt, product.getUpdatedAt());
    }

    @Test
    void setUpdatedAt_NullValue_SetsNull() {
        // Act
        product.setUpdatedAt(null);

        // Assert
        assertNull(product.getUpdatedAt());
    }

    @Test
    void getUpdatedAt_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        LocalDateTime expectedUpdatedAt = LocalDateTime.now().minusMinutes(30);
        product.setUpdatedAt(expectedUpdatedAt);

        // Act
        LocalDateTime result = product.getUpdatedAt();

        // Assert
        assertEquals(expectedUpdatedAt, result);
    }

    @Test
    void preUpdate_UpdatesUpdatedAt() {
        // Arrange
        LocalDateTime originalUpdatedAt = product.getUpdatedAt();
        
        // Wait a small amount to ensure time difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        product.preUpdate();

        // Assert
        assertTrue(product.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void toString_WithAllFields_ReturnsCorrectString() {
        // Arrange
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(new BigDecimal("999.99"));
        product.setStock(10);
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        // Act
        String result = product.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Laptop"));
        assertTrue(result.contains("Gaming laptop"));
        assertTrue(result.contains("999.99"));
        assertTrue(result.contains("10"));
    }

    @Test
    void toString_WithNullFields_ReturnsCorrectString() {
        // Act
        String result = product.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    @Test
    void setAndGetAllFields_WorksCorrectly() {
        // Arrange
        Long id = 1L;
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 25;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCreatedAt(createdAt);
        product.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
        assertEquals(createdAt, product.getCreatedAt());
        assertEquals(updatedAt, product.getUpdatedAt());
    }

    @Test
    void parameterizedConstructor_WithNullValues_HandlesCorrectly() {
        // Act
        Product newProduct = new Product(null, null, null, null);

        // Assert
        assertNotNull(newProduct);
        assertNull(newProduct.getName());
        assertNull(newProduct.getDescription());
        assertNull(newProduct.getPrice());
        assertNull(newProduct.getStock());
        assertNotNull(newProduct.getCreatedAt());
        assertNotNull(newProduct.getUpdatedAt());
    }
}

