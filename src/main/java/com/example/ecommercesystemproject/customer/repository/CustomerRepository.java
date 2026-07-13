package com.example.ecommercesystemproject.customer.repository;

import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);
    Page<Customer> findByNameContainingOrEmailContaining(String nameKeyword, String emailKeyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE (:keyword IS NULL OR c.name LIKE %:keyword% OR c.email LIKE %:keyword%) AND (:status IS NULL OR c.status = :status)")
    Page<Customer> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") CustomerStatus status, Pageable pageable);
}
