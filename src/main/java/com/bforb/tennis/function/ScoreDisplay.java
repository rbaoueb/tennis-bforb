package com.bforb.tennis.function;

import com.bforb.tennis.model.TennisGame;

/**
 * Functional interface with which we can trace the game information
 */
@FunctionalInterface
public interface ScoreDisplay<T extends TennisGame> {

    /**
     *
     * @param game {@link T TennisGame} the game information to be displayed
     */
    void display(T game);
}
