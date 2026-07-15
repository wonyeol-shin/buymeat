package com.example.ecommercesystemproject.order.repository;

public interface CustomerOrderStats {
    Long getCustomerId();
    Long getOrderCount();
    Long getTotalPrice();
}
