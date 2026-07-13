package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateProductRequest {
    @NotBlank
    private String product_name;

    @NotBlank
    private String category;

    @NotBlank
    private Long price;

    @NotBlank
    private Integer stock;

    @NotNull
    private ProductStatus status;
}
