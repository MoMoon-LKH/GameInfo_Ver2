package com.gmi.gameInfo.member.exception;

public class DuplicateMemberException extends RuntimeException{

    public DuplicateMemberException() {
        super("회원정보가 중복되는 회원이 있습니다. \n" +
                "이름, 생년월일을 다시 확인해주세요");
    }
}
