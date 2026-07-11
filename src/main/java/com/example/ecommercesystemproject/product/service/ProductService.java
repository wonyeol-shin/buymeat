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
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product(request.getProduct_name(), request.getCategory(),
                request.getPrice(), request.getStock(), request.getStatus());

        Product saveProduct = productRepository.save(product);

        return new CreateProductResponse(saveProduct.getId(), saveProduct.getProduct_name(),
                saveProduct.getCategory(), saveProduct.getPrice(), saveProduct.getStock(),
                saveProduct.getStatus());
    }

    @Transactional
    public GetProductResponse getOneProduct(Long id) {
        Product product = check(id);

        return new GetProductResponse(product.getProduct_name(), product.getCategory(), product.getPrice(),
                product.getStock(), product.getStatus());
    }

    private Product check(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 상품")
        );
    }
}
