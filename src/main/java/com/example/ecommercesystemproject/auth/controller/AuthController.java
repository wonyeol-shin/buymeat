package com.example.ecommercesystemproject.auth.controller;

import com.example.ecommercesystemproject.auth.dto.LoginRequest;
import com.example.ecommercesystemproject.auth.dto.LoginResponse;
import com.example.ecommercesystemproject.auth.dto.SignupRequest;
import com.example.ecommercesystemproject.auth.dto.SignupResponse;
import com.example.ecommercesystemproject.auth.service.AuthService;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(HttpStatus.CREATED.value(),
                "회원가입 신청이 완료되었습니다.", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest
    ) {
        LoginResponse response = authService.login(request, httpRequest);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "로그인 성공", response));
    }

    @PostMapping("/api/logout") // 요청으로인한 변경 "/logout" -> "/api/logout"
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest) {
        authService.logout(httpRequest);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "로그아웃 되었습니다."));
    }
}
