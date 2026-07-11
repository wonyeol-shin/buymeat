package com.example.ecommercesystemproject.product.service;

import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public CreateProductResponse create(CreateProductRequest request) {
        Product product = new Product(request.getProduct_name(), request.getCategory(),
                request.getPrice(), request.getStock(), request.getStatus());

        Product saveProduct = productRepository.save(product);

        return new CreateProductResponse(saveProduct.getId(), saveProduct.getProduct_name(),
                saveProduct.getCategory(), saveProduct.getPrice(), saveProduct.getStock(),
                saveProduct.getStatus());
    }
}
