package com.example.jwtapi.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtapi.entity.Product;
import com.example.jwtapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Endpoints para gestión de productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Obtener todos los productos
     * GET /api/products
     */
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Obtener un producto por ID
     * GET /api/products/{id}
     */
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID del producto a buscar", required = true)
            @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo producto
     * POST /api/products
     */
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar un producto existente
     * PUT /api/products/{id}
     */
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente con nuevos datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id, 
            @Valid @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Eliminar un producto
     * DELETE /api/products/{id}
     */
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar productos por nombre
     * GET /api/products/search?name={name}
     */
    @Operation(summary = "Buscar productos por nombre", description = "Busca productos que contengan el nombre especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(
            @Parameter(description = "Nombre del producto a buscar", required = true)
            @RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Buscar productos por rango de precio
     * GET /api/products/search/price?minPrice={minPrice}&maxPrice={maxPrice}
     */
    @Operation(summary = "Buscar productos por rango de precio", description = "Busca productos dentro de un rango de precios específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search/price")
    public ResponseEntity<List<Product>> searchProductsByPriceRange(
            @Parameter(description = "Precio mínimo del producto", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Precio máximo del producto", required = true)
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.searchProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Obtener productos con stock disponible
     * GET /api/products/stock?minStock={minStock}
     */
    @Operation(summary = "Obtener productos con stock", description = "Retorna productos que tienen stock igual o mayor al especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/stock")
    public ResponseEntity<List<Product>> getProductsWithStock(
            @Parameter(description = "Stock mínimo requerido (por defecto: 0)")
            @RequestParam(defaultValue = "0") Integer minStock) {
        List<Product> products = productService.getProductsWithStock(minStock);
        return ResponseEntity.ok(products);
    }

    /**
     * Buscar productos por nombre y precio máximo
     * GET /api/products/search/advanced?name={name}&maxPrice={maxPrice}
     */
    @Operation(summary = "Búsqueda avanzada de productos", description = "Busca productos por nombre y precio máximo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search/advanced")
    public ResponseEntity<List<Product>> searchProductsByNameAndMaxPrice(
            @Parameter(description = "Nombre del producto a buscar", required = true)
            @RequestParam String name,
            @Parameter(description = "Precio máximo del producto", required = true)
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.searchProductsByNameAndMaxPrice(name, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Contar productos con stock disponible
     * GET /api/products/count?minStock={minStock}
     */
    @Operation(summary = "Contar productos con stock", description = "Retorna el número de productos que tienen stock igual o mayor al especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> countProductsWithStock(
            @Parameter(description = "Stock mínimo requerido (por defecto: 0)")
            @RequestParam(defaultValue = "0") Integer minStock) {
        long count = productService.countProductsWithStock(minStock);
        return ResponseEntity.ok(count);
    }

    /**
     * Actualizar stock de un producto
     * PATCH /api/products/{id}/stock
     */
    @Operation(summary = "Actualizar stock de producto", description = "Actualiza únicamente el stock de un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Nuevo valor de stock", required = true)
            @RequestParam Integer stock) {
        Product updatedProduct = productService.updateStock(id, stock);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }
}
