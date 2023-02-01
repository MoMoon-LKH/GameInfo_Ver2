package com.gmi.gameInfo.member.exception;

public class DifferentAuthEmailNumberException extends RuntimeException{

    public DifferentAuthEmailNumberException() {
        super("인증번호가 다릅니다");
    }
}
