package com.gmi.gameInfo.post.exception;

public class NotFoundPostException extends RuntimeException {
    public NotFoundPostException() {
        super("해당 게시글을 찾지못하였습니다");
    }
}
