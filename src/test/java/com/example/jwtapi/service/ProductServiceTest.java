package com.example.jwtapi.service;

import com.example.jwtapi.entity.Product;
import com.example.jwtapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("999.99"), 10);
        product1.setId(1L);
        product1.setCreatedAt(LocalDateTime.now().minusDays(1));
        product1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        product2 = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"), 50);
        product2.setId(2L);
        product2.setCreatedAt(LocalDateTime.now().minusDays(2));
        product2.setUpdatedAt(LocalDateTime.now().minusDays(2));

        product3 = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("149.99"), 25);
        product3.setId(3L);
        product3.setCreatedAt(LocalDateTime.now().minusDays(3));
        product3.setUpdatedAt(LocalDateTime.now().minusDays(3));
    }

    @Test
    void getAllProducts_ReturnsAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_EmptyRepository_ReturnsEmptyList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // Act
        Optional<Product> result = productService.getProductById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(product1, result.get());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_NonExistingId_ReturnsEmpty() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.getProductById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository).findById(999L);
    }

    @Test
    void createProduct_ValidProduct_ReturnsSavedProduct() {
        // Arrange
        Product newProduct = new Product("Tablet", "Android tablet", new BigDecimal("299.99"), 15);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // Act
        Product result = productService.createProduct(newProduct);

        // Assert
        assertNotNull(result);
        assertEquals(newProduct, result);
        verify(productRepository).save(newProduct);
    }

    @Test
    void updateProduct_ExistingId_ReturnsUpdatedProduct() {
        // Arrange
        Product updatedDetails = new Product("Laptop Pro", "Updated gaming laptop", new BigDecimal("1199.99"), 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product result = productService.updateProduct(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop Pro", result.getName());
        assertEquals("Updated gaming laptop", result.getDescription());
        assertEquals(new BigDecimal("1199.99"), result.getPrice());
        assertEquals(5, result.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product1);
    }

    @Test
    void updateProduct_NonExistingId_ReturnsNull() {
        // Arrange
        Product updatedDetails = new Product("Laptop Pro", "Updated gaming laptop", new BigDecimal("1199.99"), 5);
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Product result = productService.updateProduct(999L, updatedDetails);

        // Assert
        assertNull(result);
        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ExistingId_ReturnsTrue() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = productService.deleteProduct(1L);

        // Assert
        assertTrue(result);
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NonExistingId_ReturnsFalse() {
        // Arrange
        when(productRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = productService.deleteProduct(999L);

        // Assert
        assertFalse(result);
        verify(productRepository).existsById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchProductsByName_ValidName_ReturnsMatchingProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1);
        when(productRepository.findByNameContainingIgnoreCase("laptop")).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.searchProductsByName("laptop");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepository).findByNameContainingIgnoreCase("laptop");
    }

    @Test
    void searchProductsByPriceRange_ValidRange_ReturnsMatchingProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product2, product3);
        when(productRepository.findByPriceBetween(new BigDecimal("20.00"), new BigDecimal("200.00")))
                .thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.searchProductsByPriceRange(
                new BigDecimal("20.00"), new BigDecimal("200.00"));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepository).findByPriceBetween(new BigDecimal("20.00"), new BigDecimal("200.00"));
    }

    @Test
    void getProductsWithStock_ValidMinStock_ReturnsProductsWithStock() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1, product3);
        when(productRepository.findByStockGreaterThan(20)).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getProductsWithStock(20);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepository).findByStockGreaterThan(20);
    }

    @Test
    void searchProductsByNameAndMaxPrice_ValidParams_ReturnsMatchingProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product2);
        when(productRepository.findByNameContainingAndPriceLessThanEqual("mouse", new BigDecimal("50.00")))
                .thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.searchProductsByNameAndMaxPrice("mouse", new BigDecimal("50.00"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepository).findByNameContainingAndPriceLessThanEqual("mouse", new BigDecimal("50.00"));
    }

    @Test
    void countProductsWithStock_ValidMinStock_ReturnsCount() {
        // Arrange
        when(productRepository.countByStockGreaterThan(10)).thenReturn(2L);

        // Act
        long result = productService.countProductsWithStock(10);

        // Assert
        assertEquals(2L, result);
        verify(productRepository).countByStockGreaterThan(10);
    }

    @Test
    void updateStock_ExistingId_ReturnsUpdatedProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product result = productService.updateStock(1L, 20);

        // Assert
        assertNotNull(result);
        assertEquals(20, result.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product1);
    }

    @Test
    void updateStock_NonExistingId_ReturnsNull() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Product result = productService.updateStock(999L, 20);

        // Assert
        assertNull(result);
        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_PreservesCreatedAt_UpdatesUpdatedAt() {
        // Arrange
        LocalDateTime originalCreatedAt = product1.getCreatedAt();
        LocalDateTime originalUpdatedAt = product1.getUpdatedAt();
        
        Product updatedDetails = new Product("Laptop Pro", "Updated gaming laptop", new BigDecimal("1199.99"), 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product result = productService.updateProduct(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals(originalCreatedAt, result.getCreatedAt());
        assertTrue(result.getUpdatedAt().isAfter(originalUpdatedAt));
    }
}

