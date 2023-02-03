package com.gmi.gameInfo.member.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("이미 해당 이메일을 사용중인 회원이 있습니다. \n" +
                "다른 이메일로 인증해주세요.");
    }
}
