package com.example.ecommercesystemproject.customer.repository;

import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.dashboard.dto.CustomerStatusDistribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
            SELECT c
            FROM Customer c
            WHERE
            (:keyword IS NULL OR c.name LIKE CONCAT('%',:keyword,'%') OR c.email LIKE CONCAT('%',:keyword,'%'))
            AND (:status IS NULL OR c.status = :status)""")
    Page<Customer> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") CustomerStatus status, Pageable pageable);

    // dashboard(charts) dto - CustomerStatusDistribution
    @Query("SELECT new com.example.ecommercesystemproject.dashboard.dto.CustomerStatusDistribution(c.status, COUNT(c)) " +
            "FROM Customer c GROUP BY c.status")
    List<CustomerStatusDistribution> countGroupByStatus();
}
