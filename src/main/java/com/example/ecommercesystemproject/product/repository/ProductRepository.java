package com.example.ecommercesystemproject.product.repository;

import com.example.ecommercesystemproject.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
