package com.example.ecommercesystemproject.common;

import com.example.ecommercesystemproject.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.ecommercesystemproject.common.response.ApiResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 주석 지우지 마세요
    // 공통에러 메서드 처리 이름 규칙
    // [패키지명]~~~~Exeption() 형식으로 하시면 됩니다.

    // bean validation 관련 처리(message 내용을 읽어서 List 형식으로 전달 )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> methodInvalidException(
            MethodArgumentNotValidException ex
    ) {
        List<String> message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((DefaultMessageSourceResolvable::getDefaultMessage))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // 서비스 에러 로직처리 관련 처리
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<String>> serviceException(
            ServiceException ex
    ) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        ApiResponse.of(
                                ex.getStatus().value(),
                                ex.getMessage()
                        )
                );
    }

    // 서버 에러 관련 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("서버 내부 오류가 발생했습니다.", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "서버 내부 오류가 발생했습니다."
                ));
    }

    // Auth exception handler
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEmail(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicatePhone(DuplicatePhoneException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountNotActive(AccountNotActiveException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.of(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
