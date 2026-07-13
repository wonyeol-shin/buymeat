package com.example.ecommercesystemproject.order.dto;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public class OrderResponse {
    private final Long id;
    private final String orderNumber;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final LocalDate orderDate;
    private final OrderStatus status;
    private final long totalPrice;
    private final String customerEmail;
    private final Long createdByAdminId;
    private final String createdByAdminName;
    private final String createdByAdminEmail;
    private final Role createdByAdminRole;
    private final String cancellationReason;
}
