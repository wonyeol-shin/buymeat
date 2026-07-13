package com.example.ecommercesystemproject.common.exception;

// RuntimeException -> ServiceException 상속으로 변경

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
