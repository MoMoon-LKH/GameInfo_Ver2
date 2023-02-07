package com.gmi.gameInfo.member.exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException() {
        super("아이디 혹은 비밀번호를 다시 확인해주세요");
    }
}
