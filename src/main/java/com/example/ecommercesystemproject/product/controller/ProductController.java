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

    @PostMapping
    public ResponseEntity<CreateProductResponse> createCustomer(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }
}
