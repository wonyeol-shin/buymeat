package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.response.PageResponse;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. 상품 등록
    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create(
            @Valid @RequestBody CreateProductRequest request,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        CreateProductResponse response = productService.createProduct(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    // 2. 상품 리스트 조회 (페이징 + 검색 필터링 + 정렬)
    @GetMapping("/api/products")
    public ResponseEntity<PageResponse<GetProductResponse>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order
    ) {
        PageResponse<GetProductResponse> response = productService.getAllProducts(
                keyword, category, status, page, size, sort, order
        );
        return ResponseEntity.ok(response); // 200 OK
    }

    // 3. 상품 상세 조회 (단 건 조회)
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductResponse> getOne(
            @PathVariable Long productId,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        GetProductResponse response = productService.getOneProduct(productId, adminId);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 4. 상품 정보 수정
    @PutMapping("/api/products/{productId}")
    public ResponseEntity<UpdateProductResponse> update(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        UpdateProductResponse response = productService.updateProduct(productId, request, adminId);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 5. 상품 재고 수정 (끊겼던 부분 연결 완료)
    @PatchMapping("/api/products/{productId}/stock")
    public ResponseEntity<UpdateProductStockResponse> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequest request,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        UpdateProductStockResponse response = productService.updateProductStock(productId, request, adminId);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 6. 상품 상태 수정 (추가 복구)
    @PatchMapping("/api/products/{productId}/status")
    public ResponseEntity<UpdateProductStatusResponse> updateStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequest request,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        UpdateProductStatusResponse response = productService.updateProductStatus(productId, request, adminId);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 7. 상품 삭제 (추가 복구)
    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long productId,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin
    ) {
        Long adminId = (loginAdmin == null) ? null : loginAdmin.getId();
        productService.deleteProduct(productId, adminId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}