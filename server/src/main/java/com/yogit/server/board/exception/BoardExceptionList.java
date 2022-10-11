package com.yogit.server.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum BoardExceptionList {

    NOT_FOUND_BOARD("B0001", NOT_FOUND,"존재하지 않는 Board아이디입니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
