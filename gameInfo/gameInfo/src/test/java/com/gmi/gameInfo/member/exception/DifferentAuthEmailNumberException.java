package com.gmi.gameInfo.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DifferentAuthEmailNumberException extends RuntimeException{

    public DifferentAuthEmailNumberException() {
        super("인증번호가 다릅니다");
    }
}
