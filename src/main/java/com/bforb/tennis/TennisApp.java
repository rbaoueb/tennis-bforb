package com.bforb.tennis;

import com.bforb.tennis.exception.PlayerNamingException;
import com.bforb.tennis.model.PlayerEnum;
import com.bforb.tennis.model.TennisGame;
import com.bforb.tennis.service.DisplayService;
import com.bforb.tennis.service.DisplayServiceImpl;
import com.bforb.tennis.service.TennisService;
import com.bforb.tennis.service.TennisServiceImpl;

import java.util.Arrays;
import java.util.Scanner;

public class TennisApp {


    public static void main(String[] args) {
        TennisService service = TennisServiceImpl.getInstance();
        DisplayService display = DisplayServiceImpl.getInstance();
        TennisGame game = service.startGame();
        display.printStartedGame(game);

        do {
            Scanner scanner = new Scanner(System.in);
            display.printWinnerBallRequestMessage();
            String winner = scanner.next();
            try {
                game = service.playPoint(game, winner);
            } catch (PlayerNamingException ex) {
                System.out.println("Player name should be one of A or B !");
                continue;
            }

            display.printScore(game);
        } while (!service.isGameFinished(game));
    }
}
