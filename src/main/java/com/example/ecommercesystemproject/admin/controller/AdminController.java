package com.example.ecommercesystemproject.admin.controller;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/api/admins")
    public ResponseEntity<List<GetAllAdminResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllAdmin());
    }

    @GetMapping("/api/admins/{adminId}")
    public ResponseEntity<GetOneAdminResponse> getOne(
            @PathVariable Long adminId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getOneAdmin(adminId));
    }

    @PatchMapping("/api/admins/{adminId}")
    public ResponseEntity<Void> updateOne(
            @RequestBody UpdateAdminRequest request,
            @PathVariable Long adminId) {
        adminService.updateAdminInfo(request, adminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/admins/{adminId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminStatusRequest request
    ) {
        adminService.updateAdminStatus(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/admins/{adminId}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminRoleRequest request
    ) {
        adminService.updateAdminRole(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/admins/password")
    public ResponseEntity<Void> updateMyPassword(
            @RequestBody UpdateMyPasswordRequest request
    ) {
        // 추후 세션 ID 받아서 수정 필요
        Long id = 1L;
        adminService.updateMyPassword(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/admins/profile")
    public ResponseEntity<Void> updateMyProfile(
            @RequestBody UpdateAdminRequest request
    ) {
        // 추후 세션 ID 받아서 수정 필요
        Long id = 1L;
        // 자기 자신의 프로필 업데이트를 하든지 Admin이 다른 관리자 프로필을 수정하던지 동일한 메서드 사용해오 될듯?
        adminService.updateAdminInfo(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
