package com.example.ecommercesystemproject.order.entity;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.common.BaseEntity;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private long totalPrice;

    private String cancellationReason;

    public Order(int quantity,
                 String orderNumber,
                 Admin admin,
                 Product product,
                 Customer customer,
                 long totalPrice
    ) {
        if (quantity < 1) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }
        this.status = OrderStatus.PREPARING;
        this.quantity = quantity;
        this.orderNumber = orderNumber;
        this.admin = admin;
        this.product = product;
        this.customer = customer;
        this.totalPrice = totalPrice;
    }

    public void updateStatus(OrderStatus newStatus) {
        OrderStatus nextStatus = this.status.next();

        if (nextStatus != newStatus) {
            throw new IllegalStateException(
                    "주문 상태는 준비중 → 배송중 → 배송완료 순서로만 변경할 수 있습니다."
            );
        }

        this.status = newStatus;
    }

    public void cancel(String cancellationReason) {
        if (this.status != OrderStatus.PREPARING) {
            throw new IllegalStateException(
                    "준비중 상태의 주문만 취소할 수 있습니다."
            );
        }

        this.status = OrderStatus.CANCELED;
        this.cancellationReason = cancellationReason;
    }
}
