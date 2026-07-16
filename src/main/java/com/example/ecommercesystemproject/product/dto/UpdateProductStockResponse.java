package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;

public record UpdateProductStockResponse(Long id, Integer stock, ProductStatus status) {
}
