package com.yogit.server.board.exception.clipboard;


import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ClipBoardException extends ApplicationException {

    protected ClipBoardException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
