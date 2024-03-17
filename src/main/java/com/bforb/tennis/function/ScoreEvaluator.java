package com.bforb.tennis.function;


import com.bforb.tennis.model.PlayerEnum;
import com.bforb.tennis.model.TennisGame;

/**
 * Functional interface with which we can evaluate the score
 */
@FunctionalInterface
public interface ScoreEvaluator<T extends TennisGame> {

    /**
     *
     * @param player that wins the point {@link PlayerEnum player}
     * @param game that contains the current game state {@link TennisGame game}
     * @return new {@link T game} after the new game state computed
     */
    T evaluate(PlayerEnum player, T game);
}
