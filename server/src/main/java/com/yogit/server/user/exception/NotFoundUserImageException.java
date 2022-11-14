package com.yogit.server.user.exception;

import static com.yogit.server.user.exception.UserExceptionList.NOT_FOUND_USER_IMAGE;

public class NotFoundUserImageException extends UserException{
    public NotFoundUserImageException(){
        super(NOT_FOUND_USER_IMAGE.getCODE(), NOT_FOUND_USER_IMAGE.getHTTPSTATUS(), NOT_FOUND_USER_IMAGE.getMESSAGE());
    }
}
