package com.yogit.server.user.exception.city;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LocalityExceptionList {

    // 에러 목록
    NOT_FOUND_LOCALITY("C0001", HttpStatus.NOT_FOUND, "존재하지 않는 Locality아이디입니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
