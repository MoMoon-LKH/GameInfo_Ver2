package com.gmi.gameInfo.comment.exception;

public class NotFoundCommentException extends RuntimeException{
    public NotFoundCommentException() {
        super("해당 댓글을 찾지 못하였습니다");
    }
}
