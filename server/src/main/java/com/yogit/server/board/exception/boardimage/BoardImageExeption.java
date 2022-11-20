package com.yogit.server.board.exception.boardimage;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BoardImageExeption extends ApplicationException {

    protected BoardImageExeption (String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
