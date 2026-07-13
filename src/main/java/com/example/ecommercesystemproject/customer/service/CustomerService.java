package com.example.ecommercesystemproject.customer.service;

import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.customer.dto.*;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
                savedCustomer.getStatus(), savedCustomer.getCreatedAt()
        );
    }

    // 고객 전체 조회
    @Transactional(readOnly = true)
    public Page<GetCustomerResponse> getAllCustomer(Pageable pageable, CustomerSearchCondition condition) {

        Page<Customer> customers;

        if (condition.getStatus() != null && condition.getKeyword() == null) {
            customers = customerRepository.findByStatus(condition.getStatus(), pageable);
        } else if (condition.getStatus() == null && condition.getKeyword() != null) {
           customers = customerRepository.findByNameContainingOrEmailContaining(condition.getKeyword(), condition.getKeyword(), pageable);
        } else if (condition.getStatus() != null && condition.getKeyword() != null) {
            customers = customerRepository.searchByKeywordAndStatus(condition.getKeyword(), condition.getStatus(), pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }
        return customers.map(customer -> new GetCustomerResponse(
                        customer.getId(), customer.getName(),
                        customer.getEmail(), customer.getPhone(),
                        customer.getStatus(), customer.getCreatedAt(),
                        customer.getModifiedAt()
                ));
    }

    // 고객 단건 조회
    @Transactional(readOnly = true)
    public GetCustomerResponse getOneCustomer(Long customerId) {
        Customer customer = getOrThrow(customerId);
        return new GetCustomerResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), customer.getStatus(),
                customer.getCreatedAt(), customer.getModifiedAt()
        );
    }

    // 고객 정보 수정
    @Transactional
    public UpdateCustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = getOrThrow(customerId);
        customer.updateCustomer(request.getName(), request.getEmail(), request.getPhone());
        return new UpdateCustomerResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), customer.getCreatedAt(), customer.getModifiedAt()
        );
    }

    // 고객 상태 수정
    @Transactional
    public UpdateCustomerStatusResponse updateCustomerStatus(Long customerId, UpdateCustomerStatusRequest request) {
        Customer customer = getOrThrow(customerId);
        customer.updateCustomerStatus(request.getStatus());
        return new UpdateCustomerStatusResponse(customer.getId(), customer.getStatus());
    }

    // 고객 삭제(회원탈퇴)
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer customer = getOrThrow(customerId);
        customerRepository.delete(customer);
    }

    // customer 내부 공통 메서드
    // customerId에 해당하는 고객이 없으면 예외 발생
    private Customer getOrThrow(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new ServiceException(
                        "해당 고객을 찾을 수 없습니다.", HttpStatus.NOT_FOUND
                )
        );
    }



}
