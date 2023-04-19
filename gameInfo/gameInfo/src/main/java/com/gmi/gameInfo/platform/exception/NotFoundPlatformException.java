package com.gmi.gameInfo.platform.exception;

public class NotFoundPlatformException extends RuntimeException {

    public NotFoundPlatformException() {
        super("해당 플랫폼을 찾을 수 없습니다");
    }
}
