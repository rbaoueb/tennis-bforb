package com.bforb.tennis.model;


public final record TennisGame(ScoreEnum firstPlayerScore, ScoreEnum secondPlayerScore, GameStatusEnum gameStatus) {

    public static TennisGame init(ScoreEnum firstPlayerScore, ScoreEnum secondPlayerScore, GameStatusEnum gameStatus) {
        return new TennisGame(firstPlayerScore, secondPlayerScore, gameStatus);
    }

    @Override
    public String toString() {
        return firstPlayerScore.getScoreName() + ":" + secondPlayerScore.getScoreName() + " [" + gameStatus.name() + "]";
    }

}
