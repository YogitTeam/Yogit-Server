package com.yogit.server.block.exception;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BlockException extends ApplicationException {

    protected BlockException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
