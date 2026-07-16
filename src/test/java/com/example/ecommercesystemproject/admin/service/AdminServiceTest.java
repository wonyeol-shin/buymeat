package com.example.ecommercesystemproject.admin.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.AdminSpecification;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //
class AdminServiceTest {
    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        // 1. 운영 관리자(OP) 이면서 ACTIVE 상태인 더미 데이터
        Admin opAdmin = new Admin("김운영", "op@test.com", "password", "010-1111-2222", Role.OP, "운영 목적");
        opAdmin.approve(); // 상태를 ACTIVE로 변환
        adminRepository.save(opAdmin);

        // 2. 최고 관리자(SUPER) 이면서 STANDBY(기본값) 상태인 더미 데이터
        Admin superAdmin = new Admin("박최고", "super@test.com", "password", "010-3333-4444", Role.SUPER, "최고 권한 필요");
        adminRepository.save(superAdmin);

        // 3. CS 관리자(CS) 이면서 ACTIVE 상태인 더미 데이터
        Admin csAdmin = new Admin("이시에스", "cs@test.com", "password", "010-5555-6666", Role.CS, "CS 업무");
        csAdmin.approve(); // 상태를 ACTIVE로 변환
        adminRepository.save(csAdmin);
    }

    @Test
    @DisplayName("역할이 Op인 관리자만 조회되어여 한다.")
    void findAdminDynamic_RoleFilterTest() {
        // [Given] 조건 준비
        Pageable pageable = PageRequest.of(0, 10, Sort.by("modifiedAt").descending());

        Specification<Admin> adminSpec = Specification.where(AdminSpecification.equalRole(Role.OP))
                .and(AdminSpecification.equalName(null)) // null은 알아서 무시되는지 검증
                .and(AdminSpecification.equalEmail(null))
                .and(AdminSpecification.equalStatus(null));

        // [When] 실제 만든 동적 쿼리 실행
        Page<Admin> result = adminRepository.findAll(adminSpec, pageable);

        for (Admin admin : result) {
            System.out.println(admin);
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getName()).isEqualTo("김운영");
    }

    @Test
    @DisplayName("동적 쿼리 테스트 - 상태가 ACTIVE인 관리자들만 조회되어야 한다")
    void findAdminDynamic_StatusFilterTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Admin> adminSpec = Specification.where(AdminSpecification.equalStatus(Status.ACTIVE));

        // [When] 실행
        Page<Admin> result = adminRepository.findAll(adminSpec, pageable);

        // [Then] 검증 (setUp에서 approve한 사람은 김운영, 이시에스 총 2명)
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent())
                .extracting("name")
                .containsExactlyInAnyOrder("김운영", "이시에스"); // 순서 상관없이 두 이름이 포함되어 있는지 확인
    }


    @Test
    @DisplayName("동적 쿼리 테스트 - 모든 조건이 null일 때는 전체 데이터가 조회되어야 한다")
    void findAdminDynamic_AllNullTest() {
        // [Given] 모든 조건이 null (아무것도 입력 안 한 상태)
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Admin> adminSpec = Specification.where(
                AdminSpecification.equalRole(null))
                .and(AdminSpecification.equalName(null)) // null은 알아서 무시되는지 검증
                .and(AdminSpecification.equalEmail(null))
                .and(AdminSpecification.equalStatus(null));

        // [When] 실행
        Page<Admin> result = adminRepository.findAll(adminSpec, pageable);

        // [Then] 검증 (전체 데이터 3개가 다 나와야 함)
        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
    }


}