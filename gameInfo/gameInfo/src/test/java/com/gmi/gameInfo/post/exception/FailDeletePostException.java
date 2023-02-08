package com.gmi.gameInfo.post.exception;

public class FailDeletePostException extends RuntimeException{
    public FailDeletePostException() {
        super("게시글 삭제에 실패하였습니다");
    }
}
