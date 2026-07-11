package com.example.ecommercesystemproject.customer.controller;

import com.example.ecommercesystemproject.customer.dto.CreateCustomerRequest;
import com.example.ecommercesystemproject.customer.dto.CreateCustomerResponse;
import com.example.ecommercesystemproject.customer.dto.GetCustomerResponse;
import com.example.ecommercesystemproject.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 생성
    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    // 고객 단건 조회
    @GetMapping("/{customerId}")
    public ResponseEntity<GetCustomerResponse> getOne(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getOneCustomer(customerId));
    }
}
