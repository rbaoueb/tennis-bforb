package com.bforb.tennis.service;

import com.bforb.tennis.model.TennisGame;

/**
 * service used to manager tennis game
 */
public sealed interface TennisService permits TennisServiceImpl {

    /**
     * start new game
     */
    TennisGame startGame();

    /**
     * checks if the game is already finished
     * @return boolean of the game status
     * @param game {@link TennisGame game}
     */
    boolean isGameFinished(TennisGame game);

    /**
     * play a point ball according to the winning player
     * @return {@link TennisGame game}  next game state
     * @param currentGame {@link TennisGame currentGame}
     * @param winningPlayer {@link String winningPlayer}
     */
    TennisGame playPoint(TennisGame currentGame, String winningPlayer);
}
