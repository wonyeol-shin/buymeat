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
        // 이메일 중복 체크
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 등록된 이메일입니다.");
        }
        // 전화번호 중복 체크
        if (adminRepository.existsByPhone(request.getPhone())) {
            throw new DuplicatePhoneException("이미 등록된 전화번호입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Admin 엔티티 생성(아직 DB에 저장 전, 메모리에만 존재)
        Admin admin = new Admin(
                request.getName(),
                request.getEmail(),
                encodedPassword, // 암호화된 비밀번호로 써야지
                request.getPhone(),
                request.getRole(),
                request.getWhyAdminReason()
        );
        // 실제 DB에 저장
        Admin savedAdmin = adminRepository.save(admin); // JpaRepository에서 알아서 save 구현체를 만들어 줌.

        return new SignupResponse(savedAdmin);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        // 이메일로 Admin 조회, 없으면 예외 발생
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw  new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        // 계정 생성 확인
        validateAccountStatus(admin.getStatus());

        // 세션 생성
        HttpSession session = httpRequest.getSession(true);
        // 세션에 로그인 정보 저장
        session.setAttribute(SessionConst.LOGIN_ADMIN_ID, admin.getId());
        session.setAttribute(SessionConst.LOGIN_ADMIN_EMAIL, admin.getEmail());
        session.setAttribute(SessionConst.LOGIN_ADMIN_ROLE, admin.getRole());
        // 세션 유효시간 설정
        session.setMaxInactiveInterval(SessionConst.SESSION_TIMEOUT_SECONDS);

        return new LoginResponse(admin);
    }

    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false); // 세션이 없으면 새로 만들지 않고 null 반환
        if (session != null) {
            session.invalidate(); // 서버에 저장된 세션 데이터를 완전히 삭제
        } // 세션이 원래 없었으면 그냥 아무것도 안하고 넘어감
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
