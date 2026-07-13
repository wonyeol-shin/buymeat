package com.example.ecommercesystemproject.admin.controller;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.service.AdminService;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.ecommercesystemproject.common.exception.DifferentPasswordException;
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
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    // 다건 조회 + 조건 조회, (최소 관리자 이상만 조회 가능) AdminSession은 Auth에서 추가 후 교체 필요
    @GetMapping("")
    public ResponseEntity<Page<GetAllAdminResponse>> getAll(
            @PageableDefault(
                    size = 10,
                    sort = "modifiedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) Role role,
            @RequestParam(value = "status", required = false) Status status,
            @SessionAttribute(name = "adminLogin", required = false) AdminSession adminSession
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdminDynamic(
                pageable, name, email, role, status, adminSession.getId()
        ));
    }

    // 단건조회, (최소 관리자 이상만 조회 가능)
    @GetMapping("/{adminId}")
    public ResponseEntity<GetOneAdminResponse> getOne(
            @PathVariable Long adminId,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getOneAdmin(adminId,adminSession.getId()));
    }

    // SUPER Role 계정만 가능, 원하는 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<Void> updateOne(
            @Valid @RequestBody UpdateAdminRequest request,
            @PathVariable Long adminId,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
            ) {
        adminService.updateAdminInfo(request, adminId, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // SUPER Role 계정만 가능, 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminStatusRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {
        adminService.updateAdminStatus(adminId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // SUPER Role 계정만 가능, 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {
        adminService.updateAdminRole(adminId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{adminId}/account/status")
    public ResponseEntity<Void> updateAccountStatus(
            @PathVariable Long adminId,
            @RequestBody @Valid UpdateAdminStatusRequest updateAdminStatusRequest,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {
        Long sessionAdminId = adminSession.getId();

        adminService.updateAdminAccountStatus(adminId, updateAdminStatusRequest, sessionAdminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그인 한 관리자의 패스워드 변경
    @PatchMapping("/password")
    public ResponseEntity<Void> updateMyPassword(
            @Valid @RequestBody UpdateMyPasswordRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {
        // 컨트롤러에서 패스워드 유요값 검증
        if (request.isDifferentPassword()) {
            throw new DifferentPasswordException("입력한 패스워드와 검증 패스워드 값이 다릅니다.");
        }


        adminService.updateMyPassword(request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //  로그인 한 관리자의 프로필 정보 변경
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateMyProfile(
            @Valid @RequestBody UpdateAdminRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) AdminSession adminSession
    ) {

        // 자기 자신의 프로필 업데이트를 하든지 Admin이 다른 관리자 프로필을 수정하던지 동일한 메서드 사용해오 될듯?
        adminService.updateMyInfo(request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
