package com.gmi.gameInfo.post.exception;

public class NotPostOwnerException extends RuntimeException{
    public NotPostOwnerException() {
        super("해당 게시글의 권한이 없습니다");
    }
}
