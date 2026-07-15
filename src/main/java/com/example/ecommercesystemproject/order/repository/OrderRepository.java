package com.example.ecommercesystemproject.order.repository;

import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

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

    // 고객의 주문수량 가져오기
    long countByCustomerId(Long customerId);

    // 고객의 총주문금액 가져오기
    @Query("""
    SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o 
    WHERE o.customer.id = :customerId
    """)
    Long sumTotalPriceByCustomerId(@Param("customerId") Long customerId);

    // 대시보드 Summary 오늘 주문수량 가져오기
    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            LocalDateTime start, LocalDateTime end);
}
