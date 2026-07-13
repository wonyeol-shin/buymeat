package com.example.ecommercesystemproject.order.entity;

import com.example.ecommercesystemproject.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderStatus status;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "admin_id")
//    private Admin admin;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;

    @Column(nullable = false)
    private long totalPrice;

    private String cancellationReason;

    public Order(int quantity, String orderNumber, long totalPrice) {
        this.status = OrderStatus.PREPARING;
        this.quantity = quantity;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void cancel(String cancellationReason) {
        this.status = OrderStatus.CANCELED;
        this.cancellationReason = cancellationReason;
    }
}
