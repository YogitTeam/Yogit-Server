package com.yogit.server.user.exception.city;


public class NotFoundCityException extends CityException{
    public NotFoundCityException(){
        super(CityExceptionList.NOT_FOUND_CITY.getCODE(), CityExceptionList.NOT_FOUND_CITY.getHTTPSTATUS(), CityExceptionList.NOT_FOUND_CITY.getMESSAGE());
    }
}
