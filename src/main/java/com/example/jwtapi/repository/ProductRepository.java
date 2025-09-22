package com.example.jwtapi.repository;

import com.example.jwtapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Buscar productos por nombre (case insensitive)
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Buscar productos por rango de precio
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Buscar productos con stock disponible
     */
    List<Product> findByStockGreaterThan(Integer minStock);
    
    /**
     * Buscar productos por nombre y precio máximo
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.price <= :maxPrice")
    List<Product> findByNameContainingAndPriceLessThanEqual(@Param("name") String name, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Contar productos por stock
     */
    long countByStockGreaterThan(Integer minStock);
}
