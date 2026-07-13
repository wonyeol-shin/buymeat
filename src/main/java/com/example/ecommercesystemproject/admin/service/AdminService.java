package com.example.ecommercesystemproject.admin.service;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.AdminSpecification;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.ServiceException;
import jakarta.validation.Valid;
import com.example.ecommercesystemproject.common.config.PasswordEncoder;
import com.example.ecommercesystemproject.common.exception.AccountNotActiveException;
import com.example.ecommercesystemproject.common.exception.DifferentPasswordException;
import com.example.ecommercesystemproject.common.exception.IsNotSuperAccountException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    // 로그인 한 ID 정보가 유효하지 않는 로그인 ID임
    private Admin findAdminExist(Long sessionAdminId) {
        return adminRepository.findById(sessionAdminId).orElseThrow(
                () -> new IllegalStateException("로그인 정보가 유효하지 않음")
        );
    }

    // 로그인 한 ID 정보가 "활성" 상태가 아님
    private void checkActiveAccount(Status status) {
        if (status != Status.ACTIVE) {
            throw new AccountNotActiveException("계정이 활성상태가 아닙니다. 관리자에게 문의하세요");
        }
    }

    // 로그인 한 ID 정보가 Root(super) 계정이 아님
    private void checkSuperAccount(Role role) {
        if (role != Role.SUPER) {
            throw new IsNotSuperAccountException("Super 계정만 가능한 작업입니다.");
        }
    }

    // 다건조회 + 쿼리 파라미터로 값을 받아서 정렬 and 필터 + ( 촤소한 관리자들만 이용 가능 )
    @Transactional(readOnly = true)
    public Page<GetAllAdminResponse> getAdminDynamic(
            Pageable pageable, String name, String email, Role role, Status status, Long sessionAdminId
    ) {
        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        checkActiveAccount(admin.getStatus());

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
        checkActiveAccount(admin.getStatus());

        // 찾을려는 관리자가 없는 관리자
        Admin findedAdmin = adminRepository.findById(sessionAdminId).orElseThrow(
                () ->  new ServiceException("없는 유저입니다", HttpStatus.BAD_REQUEST)
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
        checkActiveAccount(admin.getStatus());
        checkSuperAccount(admin.getRole());


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () ->  new ServiceException("없는 유저입니다", HttpStatus.BAD_REQUEST)
        );

        findedAdmin.updateProfile(request.getName(), request.getEmail(), request.getPhone());
    }

    @Transactional
    public void updateAdminAccountStatus(Long adminId, UpdateAdminStatusRequest updateAdminStatusRequest, Long sessionAdminId) {
        Admin admin = findAdminExist(sessionAdminId);

        if (admin.getStatus() != Status.ACTIVE && admin.getRole() != Role.SUPER)
            throw new IllegalStateException("변경 할 권한 없음");

        Admin findedAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ServiceException("없는 유저", HttpStatus.NOT_FOUND));

        if (findedAdmin.getStatus() != Status.STANDBY) {
            throw new ServiceException("승인 대기 상태가 아닌 계정은 승인/거부할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        findedAdmin.updateNormalStatus(updateAdminStatusRequest.getStatus());
        adminRepository.save(findedAdmin);
    }

    // 관리자 상태 변경 (Root만 가능)
    @Transactional
    public void updateAdminStatus(Long adminId, UpdateAdminStatusRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아니고 Root가 아닐경우 변경 권한 없음
        checkActiveAccount(admin.getStatus());
        checkSuperAccount(admin.getRole());


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () ->  new ServiceException("없는 유저입니다", HttpStatus.BAD_REQUEST)
        );

        // 존재하지 않는 상태로 업데이트 시도 체크
        try {
            Status status = request.getStatus();
            findedAdmin.updateNormalStatus(status);
        } catch (ServiceException e) {
            throw new ServiceException("없는 상태 입니다.",HttpStatus.BAD_REQUEST );
        }

    }

    // 관리자 정보 변경 (Root만 가능)
    @Transactional
    public void updateAdminRole(Long adminId, UpdateAdminRoleRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아니고 Root가 아닐경우 변경 권한 없음
        checkActiveAccount(admin.getStatus());
        checkSuperAccount(admin.getRole());

        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new ServiceException("없는 유저입니다", HttpStatus.BAD_REQUEST)
        );

        try {
            Role role = request.getRole();
            findedAdmin.updateRole(role);
        } catch (ServiceException e) {
            throw new ServiceException("없는 역할입니디.",HttpStatus.BAD_REQUEST );
        }
    }

    // 로그인 한 관리자 자신의 패스워드를 변경한다.
    @Transactional
    public void updateMyPassword(UpdateMyPasswordRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아님
        checkActiveAccount(admin.getStatus());

        if (!admin.getPassword().equals(request.getOldPassword())) {
            throw new DifferentPasswordException("현재 패스워드와 입력한 패스워드가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        admin.updatePassword(encodedPassword);
    }

    // 로그인 한 관리자 자신의 정보를 변경
    @Transactional
    public void updateMyInfo(UpdateAdminRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        // 로그인 한 계정이 활성 상태 계정이 아님
        checkActiveAccount(admin.getStatus());

        admin.updateProfile(request.getName(), request.getEmail(), request.getPhone());

    }

    // 승인 대기 상태의 관리자 상태를 거절한다.
    @Transactional
    public void rejectAdmin(Long adminId, @Valid RejectAdminRequest request, Long sessionAdminId) {

        // 로그인 한 계정이 존재하지 않는 id 일경우 에러, 유효하면 Admin return
        Admin admin = findAdminExist(sessionAdminId);

        checkActiveAccount(admin.getStatus());
        checkSuperAccount(admin.getRole());


        Admin findedAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new ServiceException("없는 유저입니다", HttpStatus.BAD_REQUEST)
        );

        if (findedAdmin.getStatus() != Status.STANDBY) {
            throw new ServiceException("관리자의 상태가 승인대기 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        findedAdmin.reject(request.getRejectReason());

    }
}
