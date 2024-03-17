package com.bforb.tennis.exception;


import lombok.Getter;

public class PlayerNamingException extends RuntimeException {

    @Getter
    private String playerName;

    public PlayerNamingException(String message, String playerName) {
        super(message);
        this.playerName = playerName;
    }
}
