package com.example.ecommercesystemproject.common.exception;

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
