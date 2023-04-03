package com.gmi.gameInfo.image.exception;

public class FailUploadFileException extends RuntimeException{
    public FailUploadFileException() {
        super("파일 업로드에 실패하였습니다");
    }
}
