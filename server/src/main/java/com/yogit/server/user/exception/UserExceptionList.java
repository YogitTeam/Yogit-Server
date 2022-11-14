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
    DUPLICATE_LOGIN_ID("U0002", CONFLICT,"이미 존재하는 아이디입니다."),
    NOT_FOUND_PROFILE_IMG("U0003", NOT_FOUND, "프로필 사진은 필수 값 입니다."),
    NOT_UNSUITABLE_GENDER("U0004", BAD_REQUEST, "올바르지 않은 성별 값 입니다."),
    NOT_FOUND_USER_IMAGE("U0005", NOT_FOUND, "유저의 이미지를 찾을 수 없습니다."),
    NOT_FOUND_USER_OF_USERIMAGE("U0006", NOT_FOUND, "유저와 유저이미지 이미지를 찾을 수 없습니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;

}
