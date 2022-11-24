package com.yogit.server.user.exception;

public class InvalidTokenException extends UserException{
    public InvalidTokenException(){
        super(UserExceptionList.INVALID_TOKEN.getCODE(), UserExceptionList.INVALID_TOKEN.getHTTPSTATUS(), UserExceptionList.INVALID_TOKEN.getMESSAGE());
    }

}
