package com.example.ecommercesystemproject.admin.entity;

import com.example.ecommercesystemproject.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 50 , nullable = false ,unique = true)
    private String email;

    @Setter
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 25,nullable = false ,unique = true)
    private String phone;

    // [role 종류 ]
    // SUPER : 최상위 관리자
    // OP     : 운영 관리자
    // CS      : CS 관리자
    // None     : 역할 없음
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Role role;

    // [status 종류]
    // ACTIVE : 활성
    // STANDBY : 승인대기(기본값)
    // INACTIVE : 비활성
    // SUSPENSION : 정지
    // REJECT : 승인 거부
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.STANDBY;

    @Column(length = 255, nullable = false)
    private String whyAdminReason;

    @Column(length = 255)
    private String rejectReason;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    // 회원가입
    public Admin(String name, String email, String password, String phone, Role role, String whyAdminReason) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.whyAdminReason = whyAdminReason;
    }

    // 정보변경
    public void updateProfile(String name, String email,String phone ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // 상태변경
    public void updateNormalStatus(Status status) {
        // 상태값 변경
        this.status = status;
    }

    // 거부
    public void reject(String rejectReason) {
        status = Status.REJECT;
        this.rejectReason = rejectReason;
        rejectedAt = LocalDateTime.now();
    }

    // 승인
    public void approve() {
        status = Status.ACTIVE;
        approvedAt = LocalDateTime.now();
    }

    // 역할 변경
    public void updateRole(Role role) {
        this.role = role;
    }

    // 패스워드 변경
    public void updatePassword(String password) {
        this.password = password;
    }

}
