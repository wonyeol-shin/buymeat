package com.example.ecommercesystemproject.product.entity;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.common.BaseEntity;
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
    @Column(nullable = false, length = 10)
    private ProductStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Admin admin_id;

    // 상품 등록
    public Product(String n, String c, Long p, Integer s, Admin a) {
        this.product_name = n;
        this.category = c;
        this.price = p;
        this.stock = s;
        this.admin_id = a;
        this.status = ProductStatus.ON_SALE; // 기본값 설정
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
    public void editStatus(ProductStatus s) {
        this.status = s;
    }
}
