package com.example.jwtapi.repository;

import com.example.jwtapi.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        productRepository.deleteAll();

        // Create test products
        product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("999.99"), 10);
        product1.setCreatedAt(LocalDateTime.now().minusDays(1));
        product1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        product2 = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"), 50);
        product2.setCreatedAt(LocalDateTime.now().minusDays(2));
        product2.setUpdatedAt(LocalDateTime.now().minusDays(2));

        product3 = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("149.99"), 25);
        product3.setCreatedAt(LocalDateTime.now().minusDays(3));
        product3.setUpdatedAt(LocalDateTime.now().minusDays(3));

        // Persist test data
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(product3);
    }

    @Test
    void findByNameContainingIgnoreCase_ExistingName_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("laptop");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void findByNameContainingIgnoreCase_CaseInsensitive_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("LAPTOP");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void findByNameContainingIgnoreCase_PartialName_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("ouse");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void findByNameContainingIgnoreCase_NonExistingName_ReturnsEmptyList() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameContainingIgnoreCase_EmptyString_ReturnsAllProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findByPriceBetween_ValidRange_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByPriceBetween(
                new BigDecimal("20.00"), new BigDecimal("200.00"));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Mouse")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }

    @Test
    void findByPriceBetween_ExactRange_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByPriceBetween(
                new BigDecimal("29.99"), new BigDecimal("149.99"));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Mouse")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }

    @Test
    void findByPriceBetween_NoMatches_ReturnsEmptyList() {
        // Act
        List<Product> result = productRepository.findByPriceBetween(
                new BigDecimal("2000.00"), new BigDecimal("3000.00"));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPriceBetween_SameMinMax_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByPriceBetween(
                new BigDecimal("29.99"), new BigDecimal("29.99"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void findByStockGreaterThan_ValidMinStock_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByStockGreaterThan(20);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Mouse")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }

    @Test
    void findByStockGreaterThan_ZeroMinStock_ReturnsAllProducts() {
        // Act
        List<Product> result = productRepository.findByStockGreaterThan(0);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findByStockGreaterThan_HighMinStock_ReturnsEmptyList() {
        // Act
        List<Product> result = productRepository.findByStockGreaterThan(100);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByStockGreaterThan_ExactStock_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByStockGreaterThan(10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Mouse")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }

    @Test
    void findByNameContainingAndPriceLessThanEqual_ValidParams_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingAndPriceLessThanEqual(
                "mouse", new BigDecimal("50.00"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void findByNameContainingAndPriceLessThanEqual_CaseInsensitive_ReturnsMatchingProducts() {
        // Act
        List<Product> result = productRepository.findByNameContainingAndPriceLessThanEqual(
                "MOUSE", new BigDecimal("50.00"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void findByNameContainingAndPriceLessThanEqual_PriceTooLow_ReturnsEmptyList() {
        // Act
        List<Product> result = productRepository.findByNameContainingAndPriceLessThanEqual(
                "mouse", new BigDecimal("20.00"));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameContainingAndPriceLessThanEqual_NonExistingName_ReturnsEmptyList() {
        // Act
        List<Product> result = productRepository.findByNameContainingAndPriceLessThanEqual(
                "nonexistent", new BigDecimal("1000.00"));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void countByStockGreaterThan_ValidMinStock_ReturnsCorrectCount() {
        // Act
        long result = productRepository.countByStockGreaterThan(20);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void countByStockGreaterThan_ZeroMinStock_ReturnsAllCount() {
        // Act
        long result = productRepository.countByStockGreaterThan(0);

        // Assert
        assertEquals(3L, result);
    }

    @Test
    void countByStockGreaterThan_HighMinStock_ReturnsZero() {
        // Act
        long result = productRepository.countByStockGreaterThan(100);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countByStockGreaterThan_ExactStock_ReturnsCorrectCount() {
        // Act
        long result = productRepository.countByStockGreaterThan(10);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void save_NewProduct_PersistsCorrectly() {
        // Arrange
        Product newProduct = new Product("Tablet", "Android tablet", new BigDecimal("299.99"), 15);

        // Act
        Product savedProduct = productRepository.save(newProduct);

        // Assert
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals("Tablet", savedProduct.getName());
        assertEquals("Android tablet", savedProduct.getDescription());
        assertEquals(new BigDecimal("299.99"), savedProduct.getPrice());
        assertEquals(15, savedProduct.getStock());
        assertNotNull(savedProduct.getCreatedAt());
        assertNotNull(savedProduct.getUpdatedAt());
    }

    @Test
    void findById_ExistingId_ReturnsProduct() {
        // Act
        var result = productRepository.findById(product1.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(product1.getName(), result.get().getName());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        var result = productRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ReturnsAllProducts() {
        // Act
        List<Product> result = productRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void deleteById_ExistingId_RemovesProduct() {
        // Act
        productRepository.deleteById(product1.getId());

        // Assert
        assertFalse(productRepository.findById(product1.getId()).isPresent());
        assertEquals(2, productRepository.count());
    }

    @Test
    void existsById_ExistingId_ReturnsTrue() {
        // Act
        boolean result = productRepository.existsById(product1.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_NonExistingId_ReturnsFalse() {
        // Act
        boolean result = productRepository.existsById(999L);

        // Assert
        assertFalse(result);
    }
}


