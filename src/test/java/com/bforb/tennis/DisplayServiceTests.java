package com.bforb.tennis;

import com.bforb.tennis.model.GameStatusEnum;
import com.bforb.tennis.model.ScoreEnum;
import com.bforb.tennis.model.TennisGame;
import com.bforb.tennis.service.DisplayServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@DisplayName("BforBank: Display Service")
@ExtendWith(MockitoExtension.class)
class DisplayServiceTests {

    @Mock
    TennisGame game;

    @Test
    @DisplayName("print message that game is started")
    void print_start_game_test() {
        lenient().when(game.firstPlayerScore()).thenReturn(ScoreEnum.ZERO);
        lenient().when(game.secondPlayerScore()).thenReturn(ScoreEnum.ZERO);
        DisplayServiceImpl.getInstance().printStartedGame(game);
        DisplayServiceImpl.getInstance().printWinnerBallRequestMessage();
        assertTrue(true);
    }

    @ParameterizedTest(name = "score {0}:{1} | print message for game status {2}")
    @ArgumentsSource(GameScoreData.class)
    @DisplayName("print game score with different game status")
    void print_game_score_test(ScoreEnum scoreFirstPlayer, ScoreEnum scoreSecondPlayer, GameStatusEnum gameStatus) {
        lenient().when(game.firstPlayerScore()).thenReturn(scoreFirstPlayer);
        lenient().when(game.secondPlayerScore()).thenReturn(scoreSecondPlayer);
        lenient().when(game.gameStatus()).thenReturn(gameStatus);
        DisplayServiceImpl.getInstance().printScore(game);
        assertTrue(true);
    }

    static class GameScoreData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(ScoreEnum.FORTY, ScoreEnum.ZERO, GameStatusEnum.STARTED),
                    Arguments.of(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_FIRST_PLAYER),
                    Arguments.of(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_SECOND_PLAYER),
                    Arguments.of(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.DEUCE),
                    Arguments.of(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED),
                    Arguments.of(ScoreEnum.THIRTY, ScoreEnum.WIN, GameStatusEnum.FINISHED),
                    Arguments.of(ScoreEnum.THIRTY, ScoreEnum.WIN, null)
            );
        }
    }

}
