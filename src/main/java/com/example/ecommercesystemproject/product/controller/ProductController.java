package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateProductResponse>> create(
            @Valid @RequestBody CreateProductRequest request,
            @UserInfo AdminSession adminSession
    ) {
        CreateProductResponse response = productService.createProduct(request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "상품 등록 성공",
                        response)
        ); // 201
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GetProductsResponse>>> getAll(
            @UserInfo AdminSession adminSession,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ProductSearchCondition condition
    ) {
        Page<GetProductsResponse> responses = productService.getAllProducts(adminSession.getId(), pageable, condition);
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 목록 조회 성공",
                        responses
                )
        ); // 200
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<GetProductResponse>> getOne(
            @PathVariable Long productId,
            @UserInfo AdminSession adminSession
    ) {
        GetProductResponse response = productService.getOneProduct(productId, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 상세 조회 성공",
                        response
                )
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<UpdateProductResponse>> update(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request,
            @UserInfo AdminSession adminSession
    ) {
        UpdateProductResponse response = productService.updateProduct(productId, request, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 수정 성공",
                        response
                )
        );
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ApiResponse<UpdateProductStockResponse>> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequest request,
            @UserInfo AdminSession adminSession
    ) {
        UpdateProductStockResponse response = productService.updateProductStock(productId, request, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 재고 수정 성공",
                        response
                )
        );
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ApiResponse<UpdateProductStatusResponse>> updateStatus(
            @PathVariable Long productId, @Valid @RequestBody UpdateProductStatusRequest request,
            @UserInfo AdminSession adminSession
    ) {
        UpdateProductStatusResponse response = productService.updateProductStatus(productId, request, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 상태 수정 성공",
                        response
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long productId,
            @UserInfo AdminSession adminSession
    ) {
        productService.deleteProduct(productId, adminSession.getId());
        return ResponseEntity.noContent().build(); // 204
    }
}
