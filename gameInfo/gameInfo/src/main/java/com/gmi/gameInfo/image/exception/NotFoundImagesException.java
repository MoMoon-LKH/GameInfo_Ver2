package com.gmi.gameInfo.image.exception;

public class NotFoundImagesException extends RuntimeException{
    public NotFoundImagesException() {
        super("해당 이미지를 찾지 못하였습니다");
    }
}
