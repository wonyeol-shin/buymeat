package com.example.ecommercesystemproject.product.entity;

import com.example.ecommercesystemproject.admin.entity.Admin;
// import com.example.ecommercesystemproject.common.entity.BaseTimeEntity; // 생성일, 수정일 상속 클래스가 있다면 사용
import com.example.ecommercesystemproject.common.BaseEntity;
import com.example.ecommercesystemproject.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String category;
    private Long price;
    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    private LocalDateTime createdAt; // BaseTimeEntity가 없다면 직접 선언

    // [중요] 1. 서비스의 createProduct에서 사용하는 5개짜리 생성자
    public Product(String productName, String category, Long price, Integer stock, Admin admin) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.admin = admin;
        this.status = ProductStatus.ACTIVE; // 기본 상태 설정 (필요에 따라 변경)
        this.createdAt = LocalDateTime.now();
    }

    // [중요] 2. 서비스의 updateProduct에서 사용하는 비즈니스 메서드
    public void editProduct(String productName, String category, Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    // [중요] 3. 서비스의 updateProductStock에서 사용하는 비즈니스 메서드
    public void editStock(Integer stock) {
        this.stock = stock;
        // 예시: 재고가 0이 되면 자동으로 품절(SOLD_OUT) 상태로 변경하는 로직
        if (this.stock <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    // [중요] 4. 서비스의 updateProductStatus에서 사용하는 비즈니스 메서드
    public void editStatus(ProductStatus status) {
        this.status = status;
    }


    // 재고 복구

    public void restoreStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalStateException(
                    "복구할 재고 수량은 1개 이상이어야 합니다."
            );
        }

        this.stock += quantity;

        // 단종 상품은 재고만 복구하고 상태 유지
        if (this.status == ProductStatus.DISCONTINUED) {
            return;
        }

        // 재고가 복구되면 판매 가능 상태로 전환
        this.status = ProductStatus.ACTIVE;
    }

}
