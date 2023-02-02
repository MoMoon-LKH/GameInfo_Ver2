package com.gmi.gameInfo.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundAuthEmailException extends RuntimeException{

    public NotFoundAuthEmailException() {
        super("Not Found AuthEmail");
    }
}
