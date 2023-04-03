package com.gmi.gameInfo.image.exception;

public class NotFoundFileException extends RuntimeException{

    public NotFoundFileException() {
        super("해당 파일이 존재하지않습니다");
    }
}
