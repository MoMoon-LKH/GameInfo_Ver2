package com.gmi.gameInfo.member.exception;

public class DuplicateMemberIdException extends RuntimeException{

    public DuplicateMemberIdException() {
        super("중복되는 아이디가 존재합니다");
    }
}
