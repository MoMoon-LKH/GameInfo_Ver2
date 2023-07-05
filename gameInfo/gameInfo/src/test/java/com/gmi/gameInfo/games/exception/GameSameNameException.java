package com.gmi.gameInfo.games.exception;

public class GameSameNameException extends RuntimeException{
    public GameSameNameException() {
        super("같은 이름의 게임이 존재합니다");
    }
}
