package com.yogit.server.board.exception.boardCategory;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CategoryException extends ApplicationException {

    protected CategoryException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
