package com.example.ecommercesystemproject.product.entity;

import com.example.ecommercesystemproject.common.BaseEntity;
import com.example.ecommercesystemproject.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String product_name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    //@ManyToOne(optional = false)
    //@JoinColumn(nullable = false)
    //private Admin admin_id;

    // 상품 등록
    public Product(String n, String c, Long p, Integer s, ProductStatus a) {
        this.product_name = n;
        this.category = c;
        this.price = p;
        this.stock = s;
        this.status = a;
    }

    // 상품 업데이트
    public void editProduct(String name, String ctg, Long p) {
        this.product_name = name;
        this.category = ctg;
        this.price = p;
    }

    // 재고 수정
    public void editStock(Integer s) {
        this.stock = s;
    }

    // 상태 수정
    public void editStatus(ProductStatus a) {
        this.status = a;
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
        this.status = ProductStatus.AVAILABLE;
    }

}
