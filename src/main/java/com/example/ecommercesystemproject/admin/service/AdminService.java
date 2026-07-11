package com.example.ecommercesystemproject.admin.service;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public List<GetAllAdminResponse> getAllAdmin() {
        return adminRepository.findAll().stream().map(
                (admin -> new GetAllAdminResponse(
                        admin.getId(),
                        admin.getName(),
                        admin.getEmail(),
                        admin.getStatus(),
                        admin.getCreatedAt(),
                        admin.getApprovedAt()
                ))
        ).toList();

    }

    @Transactional(readOnly = true)
    public GetOneAdminResponse getOneAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        return new GetOneAdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus()
        );
    }

    @Transactional
    public void updateAdminInfo(UpdateAdminRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        admin.updateProfile(request.getName(), request.getEmail(), request.getPhone());
    }

    @Transactional
    public void updateAdminStatus(Long adminId, UpdateAdminStatusRequest request) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        try {
            Status status = request.getStatus();
            admin.updateNormalStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("없는 상태");
        }

    }

    @Transactional
    public void updateAdminRole(Long adminId, UpdateAdminRoleRequest request) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        try {
            Role role = request.getRole();
            admin.updateRole(role);
        }catch (Exception e) {
            throw new RuntimeException("없는 역할");
        }
    }

    @Transactional
    public void updateMyPassword(UpdateMyPasswordRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 유저")
        );

        if (!request.getNewPassword().equals(request.getCheckPassword())){
            throw new IllegalStateException("변경 할 패스워드와 검증 패스워드가 다릅니다.");
        }

        if (!admin.getPassword().equals(request.getOldPassword())){
            throw new IllegalStateException("패스워드 틀림");
        }


        admin.updatePassword(request.getNewPassword());
    }


}
