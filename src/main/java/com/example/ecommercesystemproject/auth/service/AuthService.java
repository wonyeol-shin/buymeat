package com.example.ecommercesystemproject.auth.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.auth.dto.LoginRequest;
import com.example.ecommercesystemproject.auth.dto.LoginResponse;
import com.example.ecommercesystemproject.auth.dto.SignupRequest;
import com.example.ecommercesystemproject.auth.dto.SignupResponse;
import com.example.ecommercesystemproject.common.config.PasswordEncoder;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.common.exception.AccountNotActiveException;
import com.example.ecommercesystemproject.common.exception.DuplicateEmailException;
import com.example.ecommercesystemproject.common.exception.DuplicatePhoneException;
import com.example.ecommercesystemproject.common.exception.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 등록된 이메일입니다.");
        }

        if (adminRepository.existsByPhone(request.getPhone())) {
            throw new DuplicatePhoneException("이미 등록된 전화번호입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin admin = new Admin(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getRole(),
                request.getWhyAdminReason()
        );

        Admin savedAdmin = adminRepository.save(admin);
        return new SignupResponse(savedAdmin);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw  new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        validateAccountStatus(admin.getStatus());

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(SessionConst.LOGIN_ADMIN_ID, admin.getId());
        session.setAttribute(SessionConst.LOGIN_ADMIN_EMAIL, admin.getEmail());
        session.setAttribute(SessionConst.LOGIN_ADMIN_ROLE, admin.getRole());
        session.setMaxInactiveInterval(SessionConst.SESSION_TIMEOUT_SECONDS);

        return new LoginResponse(admin);
    }

    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private void validateAccountStatus(Status status) {
        switch (status) {
            case STANDBY -> throw new AccountNotActiveException("계정 승인대기 중입니다. 슈퍼 관리자의 승인을 기다려주세요.");
            case REJECT -> throw new AccountNotActiveException("계정 가입 신청이 거부되었습니다.");
            case SUSPENSION -> throw new AccountNotActiveException("정지된 계정입니다.");
            case INACTIVE -> throw new AccountNotActiveException("비활성화된 계정입니다.");
            case ACTIVE -> {
                // 로그인 가능, 통과
            }
        }
    }

}
