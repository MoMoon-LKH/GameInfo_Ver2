package com.gmi.gameInfo.games.exception;


public class NotFoundGameException extends RuntimeException{
    public NotFoundGameException() {
        super("해당 게임을 찾을 수 없습니다");
    }
}
