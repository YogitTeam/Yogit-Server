package com.yogit.server.board.exception;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BoardException extends ApplicationException {

    protected BoardException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
