package com.yogit.server.user.exception.city;


public class NotFoundLocalityException extends LocalityException {
    public NotFoundLocalityException(){
        super(LocalityExceptionList.NOT_FOUND_LOCALITY.getCODE(), LocalityExceptionList.NOT_FOUND_LOCALITY.getHTTPSTATUS(), LocalityExceptionList.NOT_FOUND_LOCALITY.getMESSAGE());
    }
}
