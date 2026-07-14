package com.example.ecommercesystemproject.product.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import com.example.ecommercesystemproject.review.dto.ListReviewResponse;
import com.example.ecommercesystemproject.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final ReviewService reviewService;

    // 상품 등록
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ServiceException("유효하지 않은 관리자", HttpStatus.NOT_FOUND));
        checkLogin(adminId);

        Product product = new Product(request.getProduct_name(), request.getCategory(),
                request.getPrice(), request.getStock(), admin);

        Product saveProduct = productRepository.save(product);
        return CreateProductResponse.from(saveProduct);
    }

    // 상품 리스트 조회
    @Transactional
    public List<GetProductsResponse> getAllProducts(Long adminId) {
        checkLogin(adminId);

        List<Product> products = productRepository.findAll();
        return products.stream().map(GetProductsResponse::from).toList();
    }

    // 상품 상세 조회
    @Transactional
    public GetProductResponse getOneProduct(Long id, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        List<ListReviewResponse> reviewList = reviewService.getAllReviewByProduct(id);
        Integer reviewCnt = reviewList.size();
        Double average = reviewList.stream()
                .mapToDouble(ListReviewResponse::getGrade)
                .average()
                .orElse(0.0);

        Map<Integer, Integer> gradeCntStats = reviewList.stream()
                .map(ListReviewResponse::getGrade)
                .collect(
                        Collectors.groupingBy(
                                grade -> grade,
                                Collectors.reducing(0, e -> 1, Integer::sum)
                        )
                );

        List<ListReviewResponse> gradeTop3 = reviewList.stream()
                .sorted(( (r1, r2) -> Double.compare(r2.getGrade(), r1.getGrade()) ))
                .limit(3)
                .toList();

        GetProductResponse getProductResponse = GetProductResponse.from(product);
        ProductDetailReview productDetailReview = new ProductDetailReview(reviewCnt, average, gradeCntStats, gradeTop3);
        getProductResponse.setReview(productDetailReview);

        return getProductResponse;
    }

    // 상품 업데이트
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editProduct(request.getProduct_name(), request.getCategory(), request.getPrice());
        return new UpdateProductResponse(product.getId());
    }

    // 상품 재고 수정
    @Transactional
    public UpdateProductStockResponse updateProductStock(Long id, UpdateProductStockRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editStock(request.getStock());
        return new UpdateProductStockResponse(product.getId(), product.getStock(), product.getStatus());
    }

    // 상품 상태 수정
    @Transactional
    public UpdateProductStatusResponse updateProductStatus(Long id, UpdateProductStatusRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editStatus(request.getStatus());
        return new UpdateProductStatusResponse(product.getId(), product.getStatus());
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long id, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        productRepository.delete(product);
    }

    // 손 댈 상품 키 선정
    private Product checkKey(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ServiceException("유효하지 않은 상품", HttpStatus.NOT_FOUND) // 404
        );
    }

    // 관리자 로그인 여부 확인
    private void checkLogin(Long loginUserId) {
        if (loginUserId == null) {
            throw new ServiceException("로그인이 필요합니다.", HttpStatus.FORBIDDEN); // 401
        }
    }
}
