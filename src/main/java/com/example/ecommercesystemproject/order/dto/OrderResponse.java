package com.example.ecommercesystemproject.order.dto;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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

    public OrderResponse(Long id, String orderNumber, String customerName, String productName, int quantity, LocalDate orderDate, OrderStatus status, long totalPrice, String customerEmail, Long createdByAdminId, String createdByAdminName, String createdByAdminEmail, Role createdByAdminRole, String cancellationReason) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customerEmail = customerEmail;
        this.createdByAdminId = createdByAdminId;
        this.createdByAdminName = createdByAdminName;
        this.createdByAdminEmail = createdByAdminEmail;
        this.createdByAdminRole = createdByAdminRole;
        this.cancellationReason = cancellationReason;
    }
}
