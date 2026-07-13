package com.example.ecommercesystemproject.common.exception;

 import com.example.ecommercesystemproject.common.ServiceException;
 import org.springframework.http.HttpStatus;

// 변경 항 패스워드와 검증 패스워드가 서로 다름
// 현재 계정의 패스워드와 입력받은 패스워드가 다름
public class DifferentPasswordException extends ServiceException {
    public DifferentPasswordException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
