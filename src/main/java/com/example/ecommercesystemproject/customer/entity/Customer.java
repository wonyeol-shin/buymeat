package com.example.ecommercesystemproject.customer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 25)
    private String phone;

    @Column(nullable = false, length = 10)
    private String status;


    public Customer(String name, String email, String phone, String status) {
        this.name = name;
        this.email =email;
        this.phone = phone;
        this.status = status;
    }

    // 고객 정보 수정
    public void updateCustomer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // 고객 상태 변경
    public void updateCustomerStatus(String status) {
        this.status = status;
    }
}
