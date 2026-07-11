package com.example.ecommercesystemproject.customer.repository;

import com.example.ecommercesystemproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
