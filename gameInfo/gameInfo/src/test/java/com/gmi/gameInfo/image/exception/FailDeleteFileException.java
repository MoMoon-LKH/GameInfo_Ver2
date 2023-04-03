package com.gmi.gameInfo.image.exception;

public class FailDeleteFileException extends RuntimeException{

    public FailDeleteFileException() {
        super("파일 삭제에 실패하였습니다");
    }
}
