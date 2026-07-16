package com.example.ecommercesystemproject.admin.controller;

import com.example.ecommercesystemproject.admin.dto.*;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.service.AdminService;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import com.example.ecommercesystemproject.common.exception.DifferentPasswordException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    @GetMapping
    public ResponseEntity<ApiResponse<Page<GetAllAdminResponse>>> getAll(
            @PageableDefault(
                    size = 10,
                    sort = "modifiedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            AdminSearchCondition condition,
            @UserInfo AdminSession adminSession
    ) {

        Page<GetAllAdminResponse> responses = adminService.getAdminDynamic(
                pageable, condition, adminSession.getId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                HttpStatus.OK.value(),
                "관리자 목록 조회 성공",
                responses)
        );

    }

    // 단건조회, (최소 관리자 이상만 조회 가능)
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<GetOneAdminResponse>> getOne(
            @PathVariable Long adminId,
            @UserInfo AdminSession adminSession
    ) {
        GetOneAdminResponse response = adminService.getOneAdmin(adminId, adminSession.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 단건 조회 성공",
                        response
                )
        );
    }

    // SUPER Role 계정만 가능, 원하는 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<ApiResponse<Void>> updateOne(
            @Valid @RequestBody UpdateAdminRequest request,
            @PathVariable Long adminId,
            @UserInfo AdminSession adminSession
    ) {
        adminService.updateAdminInfo(request, adminId, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 정보 수정 성공"
                )
        );
    }

    // SUPER Role 계정만 가능, 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminStatusRequest request,
            @UserInfo AdminSession adminSession
    ) {
        adminService.updateAdminStatus(adminId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 상태 변경 성공"
                )
        );
    }

    // SUPER Role 계정만 가능, 관리자 상태 변경
    @PatchMapping("/{adminId}/dismiss")
    public ResponseEntity<ApiResponse<Void>> rejectAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody RejectAdminRequest request,
            @UserInfo AdminSession adminSession
    ) {
        adminService.rejectAdmin(adminId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 승인 거절 성공"
                )
        );
    }

    // SUPER Role 계정만 가능, 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<ApiResponse<Void>> updateRole(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest request,
            @UserInfo AdminSession adminSession
    ) {
        adminService.updateAdminRole(adminId, request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 역할 변경 성공"
                )
        );
    }

    // 승인대기 관리자의 상태를 승인
    @PatchMapping("/{adminId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveAdmin(
            @PathVariable Long adminId,
            @UserInfo AdminSession adminSession
    ) {
        adminService.approveAdmin(adminId, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 승인 성공"
                )
        );
    }

    // 로그인 한 관리자의 패스워드 변경
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updateMyPassword(
            @Valid @RequestBody UpdateMyPasswordRequest request,
            @UserInfo AdminSession adminSession
    ) {
        // 컨트롤러에서 패스워드 유요값 검증
        if (request.isDifferentPassword()) {
            throw new DifferentPasswordException("입력한 패스워드와 검증 패스워드 값이 다릅니다.");
        }


        adminService.updateMyPassword(request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "비밀번호 변경 성공"
                )
        );
    }

    //  로그인 한 관리자의 프로필 정보 변경
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateMyProfile(
            @Valid @RequestBody UpdateAdminRequest request,
            @UserInfo AdminSession adminSession,
            HttpServletRequest httpServletRequest
    ) {

        // 자기 자신의 프로필 업데이트를 하든지 Admin이 다른 관리자 프로필을 수정하던지 동일한 메서드 사용해오 될듯?
        adminService.updateMyInfo(request, adminSession.getId());

        // email(id) 변경 후 세션과 db 정보 불일치를 방지하고자 로그아웃 진행

        HttpSession session = httpServletRequest.getSession(false); // false는 세션이 없으면 새로 만들지 말고 null을 줌
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "내 프로필 수정 성공 (이메일 변경으로 재로그인이 필요합니다.)"
                )
        );
    }

    // 관리자를 삭제(비활성화) 한다.
    @DeleteMapping("{adminId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long adminId,
            @UserInfo AdminSession adminSession
    ) {
        adminService.deleteAdmin(adminId, adminSession.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "관리자 비활성화 성공"
                )
        );
    }

}
