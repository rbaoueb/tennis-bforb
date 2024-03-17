package com.bforb.tennis.service;


import com.bforb.tennis.function.ScoreDisplay;
import com.bforb.tennis.model.GameStatusEnum;
import com.bforb.tennis.model.ScoreEnum;
import com.bforb.tennis.model.TennisGame;

public final class DisplayServiceImpl implements DisplayService {

    private static DisplayService instance;


    private DisplayServiceImpl() {
    }

    private static final ScoreDisplay<TennisGame> console = (game) -> {
        if(game.gameStatus() == GameStatusEnum.STARTED) {
            System.out.printf("Player A: %s Pts, Player B: %s Pts\n",game.firstPlayerScore().getScoreName(), game.secondPlayerScore().getScoreName());
        } else if(game.gameStatus() == GameStatusEnum.ADVANTAGE_FIRST_PLAYER) {
            System.out.println("Advantage Player A !");
        } else if(game.gameStatus() == GameStatusEnum.ADVANTAGE_SECOND_PLAYER) {
            System.out.println("Advantage Player B !");
        } else if(game.gameStatus() == GameStatusEnum.DEUCE) {
            System.out.println("DEUCE !");
        } else if(game.gameStatus() == GameStatusEnum.FINISHED) {
            if(game.firstPlayerScore() == ScoreEnum.WIN) {
                System.out.println("First Player Wins !");
            } else {
                System.out.println("Second Player Wins !");
            }
        }

    };

    public static DisplayService getInstance() {
        if (instance == null) {
            instance = new DisplayServiceImpl();
        }
        return instance;
    }


    @Override
    public void printStartedGame(TennisGame game) {
        System.out.println("""
                     _                   _                                 \s
                     | |                 (_)                                \s
                     | |_ ___ _ __  _ __  _ ___    __ _  __ _ _ __ ___   ___\s
                     | __/ _ \\ '_ \\| '_ \\| / __|  / _` |/ _` | '_ ` _ \\ / _ \\
                     | ||  __/ | | | | | | \\__ \\ | (_| | (_| | | | | | |  __/
                      \\__\\___|_| |_|_| |_|_|___/  \\__, |\\__,_|_| |_| |_|\\___|
                                                   __/ |                    \s
                                                  |___/                     \s
                """);
        System.out.printf("Game started ! Player A: %s Pts, Player B: %s Pts\n",game.firstPlayerScore().getScoreName(), game.secondPlayerScore().getScoreName());
    }

    @Override
    public void printWinnerBallRequestMessage() {
        System.out.print("Enter the winner ball player name (A/B): ");
    }

    @Override
    public void printScore(TennisGame game) {
        console.display(game);
    }
}
