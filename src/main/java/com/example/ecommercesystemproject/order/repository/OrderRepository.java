package com.example.ecommercesystemproject.order.repository;

import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    // dashboard(charts) dto - findRecentOrders
    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.product")
    Page<Order> findRecentOrders(Pageable pageable);
    // PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))로 최근 10개만 가져옴
    // ManyToOne 관계라 페이징과 JOIN FETCH를 같이 써도 안전함
           
    // 상태별 주문 개수
    Long countByStatus(OrderStatus status);

    @Query("""
        SELECT SUM(o.totalPrice)  FROM Order o\s
        WHERE ( o.createdAt > :datetime ) AND (o.status != OrderStatus.CANCELED) \s
""")
           
    Long sumTotalPriceToday(LocalDateTime datetime);
           
    // 대시보드 Summary 오늘 주문수량 가져오기
    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            LocalDateTime start, LocalDateTime end);

    // grouping by customer
    @Query("""
        SELECT
            o.customer.id customerId,
            COUNT(o) orderCount,
            COALESCE(SUM(o.totalPrice), 0) totalPrice
        FROM Order o
        WHERE o.customer.id IN :customerIds
        GROUP BY o.customer.id
    """)
    List<CustomerOrderStats> findOrderStatsGroupByCustomerIds(
            @Param("customerIds") List<Long> customerIds
    );
}
