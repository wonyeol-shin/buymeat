package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;

public record UpdateProductStatusResponse(Long id, ProductStatus status) {
}
