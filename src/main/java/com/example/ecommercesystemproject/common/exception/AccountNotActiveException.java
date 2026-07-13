package com.example.ecommercesystemproject.common.exception;

 import com.example.ecommercesystemproject.common.ServiceException;
 import org.springframework.http.HttpStatus;

// RuntimeException -> ServiceException 상속으로 변경
public class AccountNotActiveException extends ServiceException {
    public AccountNotActiveException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
