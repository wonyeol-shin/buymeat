package com.example.ecommercesystemproject.customer.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import com.example.ecommercesystemproject.customer.dto.*;
import com.example.ecommercesystemproject.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request,
            @UserInfo AdminSession adminSession
    ) {
        CreateCustomerResponse response = customerService.createCustomer(request,adminSession.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "고객 등록 성공",
                        response
                )
        );
    }

    // 고객 전체 조회(+ 조건 검색)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<GetCustomerResponse>>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            CustomerSearchCondition condition,
            @UserInfo AdminSession adminSession
    ) {
        Page<GetCustomerResponse> responses = customerService.getAllCustomer(pageable, condition, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "고객 목록 조회 성공",
                        responses
                )
        );
    }

    // 고객 단건 조회
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> getOne(
            @PathVariable Long customerId,
            @UserInfo AdminSession adminSession
    ) {
        GetCustomerResponse response = customerService.getOneCustomer(customerId, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "고객 상세 조회 성공",
                        response
                )
        );
    }

    // 고객 정보 수정
    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<UpdateCustomerResponse>> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody UpdateCustomerRequest request,
            @UserInfo AdminSession adminSession
    ) {
        UpdateCustomerResponse response = customerService.updateCustomer(customerId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "고객 정보 수정 성공",
                        response
                )
        );
    }

    // 고객 상태 변경
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse<UpdateCustomerStatusResponse>> updateCustomerStatus(
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerStatusRequest request,
            @UserInfo AdminSession adminSession
    ) {
        UpdateCustomerStatusResponse response = customerService.updateCustomerStatus(customerId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "고객 상태 변경 성공",
                        response
                )
        );
    }

    // 고객 삭제
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long customerId,
            @UserInfo AdminSession adminSession
    ) {
        customerService.deleteCustomer(customerId, adminSession.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
