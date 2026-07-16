package com.example.ecommercesystemproject.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 수정하지 마시고 상속 받아서 자유롭게 예외 처리 하십쇼
// 공통 에러 처리 클래스
@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
