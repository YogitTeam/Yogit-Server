package com.yogit.server.user.exception;

import static com.yogit.server.user.exception.UserExceptionList.NOT_FOUND_ID;

public class NotFoundUserException extends UserException{
    public NotFoundUserException(){
        super(NOT_FOUND_ID.getCODE(), NOT_FOUND_ID.getHTTPSTATUS(), NOT_FOUND_ID.getMESSAGE());
    }
}
