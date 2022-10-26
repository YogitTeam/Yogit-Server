package com.yogit.server.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@RequiredArgsConstructor
public enum UserExceptionList {

    NOT_FOUND_ID("U0001", NOT_FOUND,"존재하지 않는 아이디입니다."),
    DUPLICATE_LOGIN_ID("U0002", CONFLICT,"이미 존재하는 아이디입니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;

}
