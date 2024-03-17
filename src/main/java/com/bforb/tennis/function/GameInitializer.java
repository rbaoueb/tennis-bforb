package com.bforb.tennis.function;


import com.bforb.tennis.model.TennisGame;

/**
 * Functional interface with which we can start the game
 */
@FunctionalInterface
public interface GameInitializer<T extends TennisGame> {

    /**
     *
     * @return new {@link T TennisGame} after the game started
     */
    T start();
}
