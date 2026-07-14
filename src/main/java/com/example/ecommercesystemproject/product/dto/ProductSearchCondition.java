package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {

    private String keyword;
    private String category;
    private ProductStatus status;

    public String getKeyword() {
        return keyword == null || keyword.isBlank()
                ? null
                : keyword.trim();
    }

    public String getCategory() {
        return category == null || category.isBlank()
                ? null
                : category.trim();
    }
}
