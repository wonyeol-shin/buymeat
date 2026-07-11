package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductResponse> getOne(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getOneProduct(productId));
    }
}
