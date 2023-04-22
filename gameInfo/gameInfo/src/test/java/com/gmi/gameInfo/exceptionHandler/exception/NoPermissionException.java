package com.gmi.gameInfo.exceptionHandler.exception;

public class NoPermissionException extends RuntimeException{
    public NoPermissionException() {
        super("해당 권한이 없습니다");
    }
}
