package com.gmi.gameInfo.news.exception;

public class NotFoundNewsException extends RuntimeException{
    public NotFoundNewsException() {
        super("해당 게시물을 찾을 수 없습니다");
    }
}
