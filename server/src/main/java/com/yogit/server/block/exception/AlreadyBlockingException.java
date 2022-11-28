package com.yogit.server.block.exception;

import static com.yogit.server.block.exception.BlockExceptionList.*;

public class AlreadyBlockingException extends BlockException{

    public AlreadyBlockingException(){
        super(ALREADY_BLOCKING.getCODE(), ALREADY_BLOCKING.getHTTPSTATUS(), ALREADY_BLOCKING.getMESSAGE());
    }
}
