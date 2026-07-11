package com.example.ecommercesystemproject.customer.service;

import com.example.ecommercesystemproject.customer.dto.CreateCustomerRequest;
import com.example.ecommercesystemproject.customer.dto.CreateCustomerResponse;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 고객 생성
    //TODO : 베이스 엔티티 추가되면 생성일 넣기
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail(), request.getPhone());
        Customer savedCustomer = customerRepository.save(customer);
        return new CreateCustomerResponse(
                savedCustomer.getId(), savedCustomer.getName(),
                savedCustomer.getEmail(), savedCustomer.getPhone(),
                savedCustomer.getStatus()
        );
    }
}
