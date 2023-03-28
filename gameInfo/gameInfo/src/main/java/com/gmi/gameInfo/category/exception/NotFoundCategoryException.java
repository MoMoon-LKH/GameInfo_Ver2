package com.gmi.gameInfo.category.exception;

public class NotFoundCategoryException extends RuntimeException{
    public NotFoundCategoryException() {
        super("해당 카테고리가 없습니다");
    }
}
