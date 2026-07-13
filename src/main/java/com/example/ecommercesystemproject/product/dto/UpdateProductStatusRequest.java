package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductStatusRequest {
    @NotNull
    private ProductStatus status;
}
