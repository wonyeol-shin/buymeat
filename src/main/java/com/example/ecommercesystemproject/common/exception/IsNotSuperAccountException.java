package com.example.ecommercesystemproject.common.exception;

 import com.example.ecommercesystemproject.common.ServiceException;
 import org.springframework.http.HttpStatus;

// Root 권한이 필요한 작업에서 Root가 아닐경우 발생하는 예외
public class IsNotSuperAccountException extends ServiceException {
    public IsNotSuperAccountException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
