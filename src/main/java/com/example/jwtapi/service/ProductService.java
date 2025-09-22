package com.example.jwtapi.service;

import com.example.jwtapi.entity.Product;
import com.example.jwtapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Obtener todos los productos
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Obtener un producto por ID
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * Crear un nuevo producto
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    /**
     * Actualizar un producto existente
     */
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.preUpdate(); // Actualizar timestamp
            
            return productRepository.save(product);
        }
        
        return null;
    }
    
    /**
     * Eliminar un producto
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Buscar productos por nombre
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Buscar productos por rango de precio
     */
    public List<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * Buscar productos con stock disponible
     */
    public List<Product> getProductsWithStock(Integer minStock) {
        return productRepository.findByStockGreaterThan(minStock);
    }
    
    /**
     * Buscar productos por nombre y precio máximo
     */
    public List<Product> searchProductsByNameAndMaxPrice(String name, BigDecimal maxPrice) {
        return productRepository.findByNameContainingAndPriceLessThanEqual(name, maxPrice);
    }
    
    /**
     * Contar productos con stock disponible
     */
    public long countProductsWithStock(Integer minStock) {
        return productRepository.countByStockGreaterThan(minStock);
    }
    
    /**
     * Actualizar stock de un producto
     */
    public Product updateStock(Long id, Integer newStock) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStock(newStock);
            product.preUpdate();
            
            return productRepository.save(product);
        }
        
        return null;
    }
}
