package com.example.ecommercesystemproject.order.repository;

import com.example.ecommercesystemproject.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
