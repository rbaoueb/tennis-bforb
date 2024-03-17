package com.bforb.tennis.service;

import com.bforb.tennis.model.TennisGame;

/**
 * serice used to display message to console
 */
public sealed interface DisplayService permits DisplayServiceImpl {

    /**
     * print the new game started message
     * @param game {@link TennisGame player}
     */
    void printStartedGame(TennisGame game);

    /**
     * print to console the requested winning player name
     */
    void printWinnerBallRequestMessage();

    /**
     * print the socre of the game
     * @param game {@link TennisGame player}
     */
    void printScore(TennisGame game);
}
