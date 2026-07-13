package com.example.ecommercesystemproject.common.exception;

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

// RuntimeException -> ServiceException 상속으로 변경
public class DuplicateEmailException extends ServiceException {
    public DuplicateEmailException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
