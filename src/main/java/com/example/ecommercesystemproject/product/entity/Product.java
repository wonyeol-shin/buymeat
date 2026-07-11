package com.example.ecommercesystemproject.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
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

    @Column(nullable = false, length = 10)
    private String status;

    public Product(String n, String c, Long p, Integer s, String a) {
        this.product_name = n;
        this.category = c;
        this.price = p;
        this.stock = s;
        this.status = a;
    }
}
