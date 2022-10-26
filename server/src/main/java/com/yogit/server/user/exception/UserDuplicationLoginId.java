package com.yogit.server.user.exception;

import static com.yogit.server.user.exception.UserExceptionList.DUPLICATE_LOGIN_ID;

public class UserDuplicationLoginId extends UserException {
    public UserDuplicationLoginId() {
        super(DUPLICATE_LOGIN_ID.getCODE(), DUPLICATE_LOGIN_ID.getHTTPSTATUS(), DUPLICATE_LOGIN_ID.getMESSAGE());
    }
}
