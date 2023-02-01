package com.gmi.gameInfo.member.exception;

public class NotFoundAuthEmailException extends RuntimeException{

    public NotFoundAuthEmailException() {
        super("Not Found AuthEmail");
    }
}
