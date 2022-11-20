package com.yogit.server.user.exception;

import static com.yogit.server.user.exception.UserExceptionList.NOT_UNSUITABLE_GENDER;

public class UserGenderException extends UserException{
    public UserGenderException(){
        super(NOT_UNSUITABLE_GENDER.getCODE(), NOT_UNSUITABLE_GENDER.getHTTPSTATUS(), NOT_UNSUITABLE_GENDER.getMESSAGE());
    }
}
