package com.yogit.server.board.exception.boardCategory;

public class NotFoundCategoryException extends CategoryException {
    public NotFoundCategoryException(){
        super(CategoryExceptionList.NOT_FOUND_CATEGORY.getCODE(), CategoryExceptionList.NOT_FOUND_CATEGORY.getHTTPSTATUS(), CategoryExceptionList.NOT_FOUND_CATEGORY.getMESSAGE());
    }
}
