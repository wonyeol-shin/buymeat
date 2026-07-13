package com.example.ecommercesystemproject.common.exception;

// RuntimeException -> ServiceException 상속으로 변경

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ServiceException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
