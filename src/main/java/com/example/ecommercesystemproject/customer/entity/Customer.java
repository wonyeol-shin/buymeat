package com.example.ecommercesystemproject.customer.entity;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// BaseEntity 추가될 예정
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 25)
    private String phone;

    @Enumerated(EnumType.STRING) // enum 문자로 저장
    @Column(nullable = false, length = 10)
    private CustomerStatus status;
//    [stauts 종류]
//    ACTIVE : 활성
//    INACTIVE : 비활성
//    SUSPENSION : 정지


    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email =email;
        this.phone = phone;

        // 고객 생성시 기본 상태는 활성
        this.status = CustomerStatus.ACTIVE;
    }

    // 고객 정보 수정
    public void updateCustomer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // 고객 상태 변경
    public void updateCustomerStatus(CustomerStatus status) {
        this.status = status;
    }
}
