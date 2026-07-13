package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;

import java.time.LocalDateTime;

public record CreateProductResponse
        (Long id, String product_name, String category, Long price,
         Integer stock, ProductStatus status, LocalDateTime createdAt) {
}
