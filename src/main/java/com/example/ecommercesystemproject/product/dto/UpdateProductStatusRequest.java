package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProductStatusRequest {
    @NotBlank
    private String status;
}
