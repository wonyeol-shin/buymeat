package com.example.ecommercesystemproject.admin.service;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.AdminSpecification;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

//    @Transactional(readOnly = true)
//    public List<GetAllAdminResponse> getAllAdmin(
//
//    ) {
//        return adminRepository.findAll().stream().map(
//                (admin -> new GetAllAdminResponse(
//                        admin.getId(),
//                        admin.getName(),
//                        admin.getEmail(),
//                        admin.getStatus(),
//                        admin.getCreatedAt(),
//                        admin.getApprovedAt()
//                ))
//        ).toList();
//
//    }

    // 로그인 한 ID 정보가 유효하지 않는 로그인 ID임
    private Admin findAdminExist(Long sessionAdminId) {
        return adminRepository.findById(sessionAdminId).orElseThrow(
                () -> new IllegalStateException("로그인 정보가 유효하지 않음")
        );
    }

    // 다건조회 + 쿼리 파라미터로 값을 받아서 정렬 and 필터 + ( 촤소한 관리자들만 이용 가능 )
    @Transactional(readOnly = true)
    public Page<GetAllAdminResponse> getAdminDynamic(
            int page, int size, String name, String email, Role role, Status status, boolean active, Long sessionAdminId
    ) {
        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);
        
        // 로그인 한 계정이 활성 상태 계정이 아닐경우  조회 할 권한이 없음
        if (admin.getStatus() != Status.ACTIVE  ){
           throw  new IllegalStateException("조회 할 권한 없음");
        }

        // 수정일자의 기본값 true, false가 들어올 경우 정렬하지 않음
        Pageable pageable = !active
                ? PageRequest.of(page,size)
                : PageRequest.of(page,size, Sort.by("modifiedAt").descending());

        Specification<Admin> adminSpecification = Specification.where(
                AdminSpecification.equalName(name))
                .and(AdminSpecification.equalEmail(email))
                .and(AdminSpecification.equalRole(role))
                .and(AdminSpecification.equalStatus(status));

       return adminRepository.findAll(adminSpecification, pageable).map(
               findedAdmin -> new GetAllAdminResponse(
                       admin.getId(),
                       admin.getName(),
                       admin.getEmail(),
                       admin.getStatus(),
                       admin.getCreatedAt(),
                       admin.getApprovedAt()
               )
       );
    }

    // 단건조회
    @Transactional(readOnly = true)
    public GetOneAdminResponse getOneAdmin(Long adminId, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아닐경우  조회 할 권한이 없음
        if (admin.getStatus() != Status.ACTIVE  ){
            throw  new IllegalStateException("조회 할 권한 없음");
        }

        // 찾을려는 관리자가 없는 관리자
        Admin findedAdmin = adminRepository.findById(sessionAdminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자")
        );


        return new GetOneAdminResponse(
                findedAdmin.getId(),
                findedAdmin.getName(),
                findedAdmin.getEmail(),
                findedAdmin.getPhone(),
                findedAdmin.getRole(),
                findedAdmin.getStatus()
        );
    }

    // 관리자 정보 변경 (Root만 가능)
    @Transactional
    public void updateAdminInfo(UpdateAdminRequest request, Long adminId, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아니고 Root가 아닐경우 변경 권한 없음
        if (admin.getStatus() != Status.ACTIVE && admin.getRole() != Role.SUPER ){
            throw  new IllegalStateException("변경 할 권한 없음");
        }


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        findedAdmin.updateProfile(request.getName(), request.getEmail(), request.getPhone());
    }

    // 관리자 상태 변경 (Root만 가능)
    @Transactional
    public void updateAdminStatus(Long adminId, UpdateAdminStatusRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아니고 Root가 아닐경우 변경 권한 없음
        if (admin.getStatus() != Status.ACTIVE && admin.getRole() != Role.SUPER ){
            throw  new IllegalStateException("변경 할 권한 없음");
        }


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        // 존재하지 않는 상태로 업데이트 시도 체크
        try {
            Status status = request.getStatus();
            findedAdmin.updateNormalStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("없는 상태");
        }

    }

    // 관리자 정보 변경 (Root만 가능)
    @Transactional
    public void updateAdminRole(Long adminId, UpdateAdminRoleRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아니고 Root가 아닐경우 변경 권한 없음
        if (admin.getStatus() != Status.ACTIVE && admin.getRole() != Role.SUPER ){
            throw  new IllegalStateException("변경 할 권한 없음");
        }


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        try {
            Role role = request.getRole();
            findedAdmin.updateRole(role);
        } catch (Exception e) {
            throw new RuntimeException("없는 역할");
        }
    }

    // 로그인 한 관리자 자신의 패스워드를 변경한다.
    @Transactional
    public void updateMyPassword(UpdateMyPasswordRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아님
        if (admin.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("변경 할 권한 없음");
        }

        if (!request.getNewPassword().equals(request.getCheckPassword())) {
            throw new IllegalStateException("변경 할 패스워드와 검증 패스워드가 다릅니다.");
        }

        if (!admin.getPassword().equals(request.getOldPassword())) {
            throw new IllegalStateException("패스워드 틀림");
        }

        admin.updatePassword(request.getNewPassword());
    }

    // 로그인 한 관리자 자신의 정보를 변경
    @Transactional
    public void updateMyInfo(UpdateAdminRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아님
        if (admin.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("변경 할 권한 없음");
        }

        admin.updateProfile(request.getName(), request.getEmail(), request.getPhone());

    }



}
