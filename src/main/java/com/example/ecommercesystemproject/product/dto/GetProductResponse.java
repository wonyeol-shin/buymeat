package com.example.ecommercesystemproject.product.dto;

public record GetProductResponse
        (String product_name, String category, Long price,
         Integer stock, String status) {
}
