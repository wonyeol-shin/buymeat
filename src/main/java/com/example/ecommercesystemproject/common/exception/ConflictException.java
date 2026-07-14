package com.example.ecommercesystemproject.common.exception;

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ServiceException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
