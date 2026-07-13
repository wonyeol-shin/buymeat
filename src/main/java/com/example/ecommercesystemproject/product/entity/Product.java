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

    // 주석 해제 + admin_id -> admin 으로 필드명 변경 (연관관계 필드는 객체를 그대로 가리키는게 컨벤션)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // 상품 등록 (admin 파라미터 추가됨 - 등록 관리자 저장을 위해 필수)
    // 변수명을... 한글자로 쓰는거.. 괜찮을까요....???
    public Product(String n, String c, Long p, Integer s, String a, Admin admin) {
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Admin admin_id;

    // 상품 등록
    public Product(String n, String c, Long p, Integer s, Admin a) {
        this.product_name = n;
        this.category = c;
        this.price = p;
        this.stock = s;
        this.status = a;
        this.admin = admin;
        this.admin_id = a;
        this.status = ProductStatus.ON_SALE; // 기본값 설정
    }

    // 상품 업데이트
    public void editProduct(String name, String ctg, Long p) {
        this.product_name = name;
        this.category = ctg;
        this.price = p;
    }

    // 재고 수정 + 상태 자동 전환
    // 재고 0 이하 -> SOLD_OUT
    // 재고 1 이상 -> ON_SALE
    // 현재 상태가 DISCONTINUED(단종)이면 재고 값만 바뀌고 상태는 유지
    public void editStock(Integer newStock) {
        this.stock = newStock;

        if ("DISCONTINUED".equals(this.status)) { // // ENUM을 무엇으로 하는지 보고 수정 필요.
            return;
        }

        this.status = (newStock <= 0) ? "SOLD_OUT" : "ON_SALE"; // ENUM을 무엇으로 하는지 보고 수정 필요.
    }

    // 상태 수정
    public void editStatus(ProductStatus s) {
        this.status = s;
    }
}
// 브랜치 rebase 후 다시 푸쉬