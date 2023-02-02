package com.yogit.server.user.exception.city;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class LocalityException extends ApplicationException {

    protected LocalityException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
