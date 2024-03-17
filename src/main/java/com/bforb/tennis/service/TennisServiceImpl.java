package com.bforb.tennis.service;


import com.bforb.tennis.exception.GameAlreadyFinishedException;
import com.bforb.tennis.exception.PlayerNamingException;
import com.bforb.tennis.function.GameInitializer;
import com.bforb.tennis.function.ScoreEvaluator;
import com.bforb.tennis.model.GameStatusEnum;
import com.bforb.tennis.model.PlayerEnum;
import com.bforb.tennis.model.ScoreEnum;
import com.bforb.tennis.model.TennisGame;

import java.util.Arrays;

public final class TennisServiceImpl implements TennisService {

    private static TennisService instance;

    private TennisServiceImpl(){}

    private static final GameInitializer<TennisGame> game = () -> TennisGame.init(ScoreEnum.ZERO, ScoreEnum.ZERO, GameStatusEnum.STARTED);


    private static final ScoreEvaluator<TennisGame> evaluator = (winningPlayer, currentScore) -> {
        if(winningPlayer == PlayerEnum.A) {
            return switch (currentScore.gameStatus()) {
                case STARTED -> {
                    if(currentScore.firstPlayerScore() == ScoreEnum.FORTY && currentScore.secondPlayerScore() == ScoreEnum.FORTY) {
                        yield TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.ADVANTAGE_FIRST_PLAYER);
                    }
                    if(currentScore.firstPlayerScore() == ScoreEnum.FORTY) {
                        yield TennisGame.init(ScoreEnum.WIN, currentScore.secondPlayerScore(), GameStatusEnum.FINISHED);
                    }
                    yield TennisGame.init(currentScore.firstPlayerScore().getNextScore(), currentScore.secondPlayerScore(), GameStatusEnum.STARTED);
                }
                case DEUCE -> TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.ADVANTAGE_FIRST_PLAYER);
                case ADVANTAGE_FIRST_PLAYER -> TennisGame.init(ScoreEnum.WIN, currentScore.secondPlayerScore(), GameStatusEnum.FINISHED);
                case ADVANTAGE_SECOND_PLAYER -> TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.DEUCE);
                case FINISHED -> throw new GameAlreadyFinishedException("The game is already finished !");
            };
        } else {
            return switch (currentScore.gameStatus()) {
                case STARTED -> {
                    if(currentScore.firstPlayerScore() == ScoreEnum.FORTY && currentScore.secondPlayerScore() == ScoreEnum.FORTY) {
                        yield TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.ADVANTAGE_SECOND_PLAYER);
                    }
                    if(currentScore.secondPlayerScore() == ScoreEnum.FORTY) {
                        yield TennisGame.init(currentScore.firstPlayerScore(), ScoreEnum.WIN, GameStatusEnum.FINISHED);
                    }
                    yield TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore().getNextScore(), GameStatusEnum.STARTED);
                }
                case DEUCE -> TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.ADVANTAGE_SECOND_PLAYER);
                case ADVANTAGE_SECOND_PLAYER -> TennisGame.init(currentScore.firstPlayerScore(), ScoreEnum.WIN, GameStatusEnum.FINISHED);
                case ADVANTAGE_FIRST_PLAYER -> TennisGame.init(currentScore.firstPlayerScore(), currentScore.secondPlayerScore(), GameStatusEnum.DEUCE);
                case FINISHED -> throw new GameAlreadyFinishedException("The game is already finished !");
            };
        }

    };

    public static TennisService getInstance() {
        if (instance == null) {
            instance = new TennisServiceImpl();
        }
        return instance;
    }

    @Override
    public TennisGame startGame() {
        return game.start();
    }

    @Override
    public boolean isGameFinished(TennisGame score) {
        return score.firstPlayerScore() == ScoreEnum.WIN || score.secondPlayerScore() == ScoreEnum.WIN;
    }

    @Override
    public TennisGame playPoint(TennisGame currentScore, String winningPlayer) {
        PlayerEnum winner = Arrays.stream(PlayerEnum.values()).filter(e -> e.name().equalsIgnoreCase(winningPlayer)).findAny().orElseThrow(() -> new PlayerNamingException("player name not recognized", winningPlayer));
        return evaluator.evaluate(winner, currentScore);
    }

}
