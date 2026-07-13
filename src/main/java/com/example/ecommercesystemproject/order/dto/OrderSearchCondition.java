package com.example.ecommercesystemproject.order.dto;

import com.example.ecommercesystemproject.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchCondition {

    private String keyword;
    private OrderStatus status;

    public String getKeyword() {
        return keyword == null || keyword.isBlank()
                ? null
                : keyword.trim();
    }

}
