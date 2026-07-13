package com.example.ecommercesystemproject.common.exception;

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ServiceException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
