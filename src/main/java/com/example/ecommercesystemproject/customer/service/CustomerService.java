package com.example.ecommercesystemproject.customer.service;

import com.example.ecommercesystemproject.customer.dto.CreateCustomerRequest;
import com.example.ecommercesystemproject.customer.dto.CreateCustomerResponse;
import com.example.ecommercesystemproject.customer.dto.GetCustomerResponse;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 고객 생성
    //TODO : 베이스 엔티티 추가되면 생성일 넣기
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail(), request.getPhone());
        Customer savedCustomer = customerRepository.save(customer);
        return new CreateCustomerResponse(
                savedCustomer.getId(), savedCustomer.getName(),
                savedCustomer.getEmail(), savedCustomer.getPhone(),
                savedCustomer.getStatus()
        );
    }

    // 고객 단건 조회
    @Transactional(readOnly = true)
    public GetCustomerResponse getOneCustomer(Long customerId) {
        Customer customer = getOrThrow(customerId);
        return new GetCustomerResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), customer.getStatus()
        );
    }

    // customer 내부 공통 메서드
    // customerId에 해당하는 고객이 없으면 예외 발생
    private Customer getOrThrow(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다. "
                )
        );
    }
}
