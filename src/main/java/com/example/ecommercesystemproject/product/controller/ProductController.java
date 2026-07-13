package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request)); // 201
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<GetProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts()); // 200
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductResponse> getOne(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getOneProduct(productId));
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<UpdateProductResponse> update
            (@PathVariable Long productId, @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }

    @PatchMapping("/api/products/{productId}/stock")
    public ResponseEntity<UpdateProductStockResponse> updateStock
            (@PathVariable Long productId, @RequestBody UpdateProductStockRequest request) {
        return ResponseEntity.ok(productService.updateProductStock(productId, request));
    }

    @PatchMapping("/api/products/{productId}/status")
    public ResponseEntity<UpdateProductStatusResponse> updateStatus
            (@PathVariable Long productId, @RequestBody UpdateProductStatusRequest request) {
        return ResponseEntity.ok(productService.updateProductStatus(productId, request));
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build(); // 204
    }
}
