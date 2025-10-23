package com.example.jwtapi.controller;

import com.example.jwtapi.entity.Product;
import com.example.jwtapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

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
    }

    @Test
    void getAllProducts_ReturnsAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));
    }

    @Test
    void getAllProducts_EmptyList_ReturnsEmptyArray() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() throws Exception {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void getProductById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ValidProduct_ReturnsCreatedProduct() throws Exception {
        // Arrange
        Product newProduct = new Product("Tablet", "Android tablet", new BigDecimal("299.99"), 15);
        when(productService.createProduct(any(Product.class))).thenReturn(newProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.price").value(299.99))
                .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    void createProduct_InvalidProduct_ReturnsBadRequest() throws Exception {
        // Arrange
        Product invalidProduct = new Product("", "", new BigDecimal("-1"), -1);
        when(productService.createProduct(any(Product.class)))
                .thenThrow(new RuntimeException("Invalid product"));

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_MissingFields_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProduct_ExistingId_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        Product updatedProduct = new Product("Laptop Pro", "Updated gaming laptop", new BigDecimal("1199.99"), 5);
        when(productService.updateProduct(1L, any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.price").value(1199.99))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void updateProduct_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Product updatedProduct = new Product("Laptop Pro", "Updated gaming laptop", new BigDecimal("1199.99"), 5);
        when(productService.updateProduct(999L, any(Product.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_ExistingId_ReturnsNoContent() throws Exception {
        // Arrange
        when(productService.deleteProduct(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(productService.deleteProduct(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchProductsByName_ValidName_ReturnsMatchingProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1);
        when(productService.searchProductsByName("laptop")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/search")
                .param("name", "laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void searchProductsByPriceRange_ValidRange_ReturnsMatchingProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product2);
        when(productService.searchProductsByPriceRange(new BigDecimal("20.00"), new BigDecimal("50.00")))
                .thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/search/price")
                .param("minPrice", "20.00")
                .param("maxPrice", "50.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Mouse"));
    }

    @Test
    void getProductsWithStock_ValidMinStock_ReturnsProductsWithStock() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1);
        when(productService.getProductsWithStock(5)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/stock")
                .param("minStock", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getProductsWithStock_DefaultMinStock_ReturnsProductsWithStock() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getProductsWithStock(0)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void searchProductsByNameAndMaxPrice_ValidParams_ReturnsMatchingProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product2);
        when(productService.searchProductsByNameAndMaxPrice("mouse", new BigDecimal("50.00")))
                .thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/search/advanced")
                .param("name", "mouse")
                .param("maxPrice", "50.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Mouse"));
    }

    @Test
    void countProductsWithStock_ValidMinStock_ReturnsCount() throws Exception {
        // Arrange
        when(productService.countProductsWithStock(10)).thenReturn(2L);

        // Act & Assert
        mockMvc.perform(get("/api/products/count")
                .param("minStock", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void countProductsWithStock_DefaultMinStock_ReturnsCount() throws Exception {
        // Arrange
        when(productService.countProductsWithStock(0)).thenReturn(5L);

        // Act & Assert
        mockMvc.perform(get("/api/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void updateStock_ExistingId_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        Product updatedProduct = new Product("Laptop", "Gaming laptop", new BigDecimal("999.99"), 20);
        when(productService.updateStock(1L, 20)).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(patch("/api/products/1/stock")
                .param("stock", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    void updateStock_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(productService.updateStock(999L, 20)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(patch("/api/products/999/stock")
                .param("stock", "20"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_InvalidJson_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProduct_InvalidJson_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }
}

