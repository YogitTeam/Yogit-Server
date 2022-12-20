package com.yogit.server.board.exception;

import static com.yogit.server.board.exception.BoardExceptionList.*;

public class InvalidMyClubTypeException extends BoardException{
    public InvalidMyClubTypeException(){
        super(INVALID_MYCLUBTYPE.getCODE(), INVALID_MYCLUBTYPE.getHTTPSTATUS(), INVALID_MYCLUBTYPE.getMESSAGE());
    }
}
