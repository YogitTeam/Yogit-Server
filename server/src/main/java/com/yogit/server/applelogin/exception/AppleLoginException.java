package com.yogit.server.applelogin.exception;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AppleLoginException extends ApplicationException {

    protected AppleLoginException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
