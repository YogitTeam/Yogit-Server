package com.yogit.server.user.exception;


import static com.yogit.server.user.exception.UserExceptionList.NOT_FOUND_PROFILE_IMG;

public class NotFoundUserProfileImg extends UserException{
    public NotFoundUserProfileImg(){
        super(NOT_FOUND_PROFILE_IMG.getCODE(), NOT_FOUND_PROFILE_IMG.getHTTPSTATUS(), NOT_FOUND_PROFILE_IMG.getMESSAGE());
    }
}
