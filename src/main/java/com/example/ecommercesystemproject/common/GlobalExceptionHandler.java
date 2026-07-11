package com.example.ecommercesystemproject.common;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
    public ResponseEntity<String> serviceException(
            ServiceException ex
    ) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

}
