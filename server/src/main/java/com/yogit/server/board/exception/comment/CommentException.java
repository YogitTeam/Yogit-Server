package com.yogit.server.board.exception.comment;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CommentException extends ApplicationException {

    protected CommentException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
