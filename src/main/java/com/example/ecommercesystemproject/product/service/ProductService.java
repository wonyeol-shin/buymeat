package com.example.ecommercesystemproject.product.service;

import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product(request.getProduct_name(), request.getCategory(),
                request.getPrice(), request.getStock(), request.getStatus());

        Product saveProduct = productRepository.save(product);

        return new CreateProductResponse(saveProduct.getId(), saveProduct.getProduct_name(),
                saveProduct.getCategory(), saveProduct.getPrice(), saveProduct.getStock(),
                saveProduct.getStatus(), saveProduct.getCreatedAt());
    }

    // 상품 다 건 조회
    @Transactional
    public List<GetProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(a -> new GetProductResponse(a.getId(), a.getProduct_name(), a.getCategory(),
                        a.getPrice(), a.getStock(), a.getStatus(), a.getCreatedAt()))
                .toList();
    }

    // 상품 단 건 조회
    @Transactional
    public GetProductResponse getOneProduct(Long id) {
        Product product = check(id);

        return new GetProductResponse(product.getId(), product.getProduct_name(), product.getCategory(), product.getPrice(),
                product.getStock(), product.getStatus(), product.getCreatedAt());
    }

    // 상품 업데이트
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = check(id);

        product.editProduct(request.getProduct_name(), request.getCategory(), request.getPrice());
        return new UpdateProductResponse(product.getId());
    }

    // 상품 재고 수정
    @Transactional
    public UpdateProductStockResponse updateProductStock(Long id, UpdateProductStockRequest request) {
        Product product = check(id);

        product.editStock(request.getStock());
        return new UpdateProductStockResponse(product.getId(), product.getStock(), product.getStatus());
    }

    // 상품 상태 수정
    @Transactional
    public UpdateProductStatusResponse updateProductStatus(Long id, UpdateProductStatusRequest request) {
        Product product = check(id);

        product.editStatus(request.getStatus());
        return new UpdateProductStatusResponse(product.getId(), product.getStatus());
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        Product product = check(id);

        productRepository.delete(product);
    }

    // 손 댈 상품 키 선정
    private Product check(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ServiceException("유효하지 않은 상품", HttpStatus.NOT_FOUND) // 404
        );
    }
}
