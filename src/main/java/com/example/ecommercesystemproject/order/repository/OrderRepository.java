package com.example.ecommercesystemproject.order.repository;

import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
    SELECT o FROM Order o WHERE
    (:keyword IS NULL OR o.orderNumber LIKE CONCAT('%', :keyword, '%')
    OR o.customer.name LIKE CONCAT('%', :keyword, '%')) AND (:status IS NULL 
    OR o.status = :status)""")
    Page<Order> findByKeywordAndStatus(
            @Param("keyword") String keyword,
            @Param("status")OrderStatus status,
            Pageable pageable
            );
}
