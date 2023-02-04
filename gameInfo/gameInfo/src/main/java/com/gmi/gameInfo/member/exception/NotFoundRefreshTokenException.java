package com.gmi.gameInfo.member.exception;

public class NotFoundRefreshTokenException extends RuntimeException{
    public NotFoundRefreshTokenException() {
        super("해당 회원의 토큰 정보를 찾을 수 없습니다");
    }
}
