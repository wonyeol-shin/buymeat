package com.example.ecommercesystemproject.customer.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.common.exception.AccountNotActiveException;
import com.example.ecommercesystemproject.common.exception.ForbiddenException;
import com.example.ecommercesystemproject.common.exception.IsNotSuperAccountException;
import com.example.ecommercesystemproject.customer.dto.*;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.order.repository.CustomerOrderStats;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final AdminRepository adminRepository;

    private Admin findAdminExist(Long sessionAdminId) {
        return adminRepository.findById(sessionAdminId).orElseThrow(
                () -> new IllegalStateException("로그인 정보가 유효하지 않음")
        );
    }

    private void checkActiveAccount(Status status) {
        if (status != Status.ACTIVE) {
            throw new AccountNotActiveException("계정이 활성상태가 아닙니다. 관리자에게 문의하세요");
        }
    }

    private void checkSuperAccount(Role role) {
        if (role != Role.SUPER) {
            throw new IsNotSuperAccountException("Super 계정만 가능한 작업입니다.");
        }
    }


    // 고객 생성
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request, Long adminId) {
        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());

        Customer customer = new Customer(request.getName(), request.getEmail(), request.getPhone());
        Customer savedCustomer = customerRepository.save(customer);
        return new CreateCustomerResponse(
                savedCustomer.getId(), savedCustomer.getName(),
                savedCustomer.getEmail(), savedCustomer.getPhone(),
                savedCustomer.getStatus(), savedCustomer.getCreatedAt()
        );
    }

    // 고객 전체 조회 + 각 고객 주문수량, 총주문금액
    @Transactional(readOnly = true)
    public Page<GetCustomerResponse> getAllCustomer(Pageable pageable, CustomerSearchCondition condition, Long adminId) {

        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());

        Page<Customer> customers = customerRepository.searchByKeywordAndStatus(condition.getKeyword(), condition.getStatus(), pageable);

        // customer ID list
        List<Long> customerIds = customers.map(Customer::getId).toList();

        // customers 대상으로 group 으로 한방에 조회
        Map<Long, CustomerOrderStats> customerOrderStats = orderRepository.findOrderStatsGroupByCustomerIds(customerIds)
                .stream()
                .collect(Collectors.toMap(CustomerOrderStats::getCustomerId, stats -> stats));

        return customers.map(customer -> {
            CustomerOrderStats coStat = customerOrderStats.get(customer.getId());
            return new GetCustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getStatus(),
                    coStat == null ? 0 : coStat.getOrderCount(),
                    coStat == null ? 0 : coStat.getTotalPrice(),
                    customer.getCreatedAt(),
                    customer.getModifiedAt()
            );
        });
    }

    // 고객 단건 조회 + 해당 고객 주문수량, 총 주문금액
    @Transactional(readOnly = true)
    public GetCustomerResponse getOneCustomer(Long customerId, Long adminId) {
        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());

        Customer customer = getOrThrow(customerId);
        Long orderCount = orderRepository.countByCustomerId(customerId);
        Long totalPrice = orderRepository.sumTotalPriceByCustomerId(customerId);
        return new GetCustomerResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), customer.getStatus(), orderCount, totalPrice,
                customer.getCreatedAt(), customer.getModifiedAt()
        );
    }

    // 고객 정보 수정
    @Transactional
    public UpdateCustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request, Long adminId) {
        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());

        Customer customer = getOrThrow(customerId);
        customer.updateCustomer(request.getName(), request.getEmail(), request.getPhone());
        return new UpdateCustomerResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), customer.getCreatedAt(), customer.getModifiedAt()
        );
    }

    // 고객 상태 수정
    @Transactional
    public UpdateCustomerStatusResponse updateCustomerStatus(Long customerId, UpdateCustomerStatusRequest request, Long adminId) {
        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());

        Customer customer = getOrThrow(customerId);
        customer.updateCustomerStatus(request.getStatus());
        return new UpdateCustomerStatusResponse(customer.getId(), customer.getStatus());
    }

    // 고객 삭제(회원탈퇴) - 비활성으로 상태 변경
    @Transactional
    public void deleteCustomer(Long customerId, Long adminId) {
        Admin admin = findAdminExist(adminId);
        checkActiveAccount(admin.getStatus());
        checkSuperAccount(admin.getRole());

        Customer customer = getOrThrow(customerId);
        customer.inactiveCustomerStatus();
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
