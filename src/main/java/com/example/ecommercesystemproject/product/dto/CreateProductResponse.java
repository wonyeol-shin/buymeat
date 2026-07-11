package com.example.ecommercesystemproject.product.dto;

public record CreateProductResponse
        (Long id, String product_name, String category, Long price,
         Integer stock, String status) {
}
