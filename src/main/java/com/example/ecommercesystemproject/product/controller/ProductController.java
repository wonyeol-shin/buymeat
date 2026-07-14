package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<ApiResponse<CreateProductResponse>> create
            (@Valid @RequestBody CreateProductRequest request,
             @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

     
        CreateProductResponse response = productService.createProduct(request, sessionAdminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "상품 등록 성공",
                        response)
        ); // 201
    }

    @GetMapping("/api/products")
    public ResponseEntity<ApiResponse<List<GetProductsResponse>>> getAll
            (@Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

       
        List<GetProductsResponse> responses = productService.getAllProducts(sessionAdminId);
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 목록 조회 성공",
                        responses
                )
        ); // 200
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ApiResponse<GetProductResponse>> getOne
            (@PathVariable Long productId,
             @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID, required = false) AdminSession loginAdmin) {

       
        GetProductResponse response = productService.getOneProduct(productId, sessionAdminId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 상세 조회 성공",
                        response
                )
        );
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ApiResponse<UpdateProductResponse>> update
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductRequest request,
             @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

     
        UpdateProductResponse response = productService.updateProduct(productId, request, sessionAdminId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 수정 성공",
                        response
                )
        );
    }

    @PatchMapping("/api/products/{productId}/stock")
    public ResponseEntity<ApiResponse<UpdateProductStockResponse>> updateStock
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductStockRequest request,
             @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

        
        UpdateProductStockResponse response = productService.updateProductStock(productId, request, sessionAdminId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 재고 수정 성공",
                        response
                )
        );
    }

    @PatchMapping("/api/products/{productId}/status")
    public ResponseEntity<ApiResponse<UpdateProductStatusResponse>> updateStatus
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductStatusRequest request,
             @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

       
        UpdateProductStatusResponse response = productService.updateProductStatus(productId, request, sessionAdminId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "상품 상태 수정 성공",
                        response
                )
        );
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> delete
            (@PathVariable Long productId,
             @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
            ) {

        productService.deleteProduct(productId, sessionAdminId);
        return ResponseEntity.noContent().build(); // 204
    }
}
