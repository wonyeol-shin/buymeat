package com.example.ecommercesystemproject.admin.repository;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

    // AuthService에서 사용
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Admin> findByEmail(String email);

    // 대시보드 Summary 활성화된 관리자 수 가져오기
    long countByStatus(Status status);

}
