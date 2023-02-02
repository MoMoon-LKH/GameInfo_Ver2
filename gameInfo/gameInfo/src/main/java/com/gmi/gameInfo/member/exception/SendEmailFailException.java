package com.gmi.gameInfo.member.exception;


public class SendEmailFailException extends RuntimeException {
    public SendEmailFailException() {
        super("이메일 전송에 실패하였습니다");
    }
}
