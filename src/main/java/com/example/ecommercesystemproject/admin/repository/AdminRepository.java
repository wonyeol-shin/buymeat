package com.example.ecommercesystemproject.admin.repository;

import com.example.ecommercesystemproject.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // AuthService에서 사용
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Admin> findByEmail(String email);

}
