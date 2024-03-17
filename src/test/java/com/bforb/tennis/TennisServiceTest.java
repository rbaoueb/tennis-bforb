package com.bforb.tennis;

import com.bforb.tennis.exception.GameAlreadyFinishedException;
import com.bforb.tennis.exception.GenericGameException;
import com.bforb.tennis.exception.PlayerNamingException;
import com.bforb.tennis.model.GameStatusEnum;
import com.bforb.tennis.model.ScoreEnum;
import com.bforb.tennis.model.TennisGame;
import com.bforb.tennis.service.TennisServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.internal.matchers.Null;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BforBank: Tennis Service")
class TennisServiceTest {

    @Test
    @DisplayName("Test score after the game is started")
    void tennis_game_start_test() {
        TennisGame newGame = TennisServiceImpl.getInstance().startGame();
        assertEquals(ScoreEnum.ZERO, newGame.firstPlayerScore());
        assertEquals(ScoreEnum.ZERO, newGame.secondPlayerScore());
        assertFalse(TennisServiceImpl.getInstance().isGameFinished(newGame));
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball | new score {2}")
    @ArgumentsSource(BallAfterSimpleScoreData.class)
    @DisplayName("simple game without deuce and advantage")
    void simple_game_without_deuce_test(TennisGame startingScore, String scoringPlayer, TennisGame expectedScore) {
        assertNotEquals(GameStatusEnum.ADVANTAGE_FIRST_PLAYER, expectedScore.gameStatus(), "shouldn't have advantage!");
        assertNotEquals(GameStatusEnum.ADVANTAGE_SECOND_PLAYER, expectedScore.gameStatus(), "shouldn't have advantage!");
        assertNotEquals(GameStatusEnum.FINISHED, expectedScore.gameStatus(), "shouldn't finished!");
        assertEquals(expectedScore, TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer));
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball | new score {2}")
    @ArgumentsSource(FinalScoreWithoutDeuceData.class)
    @DisplayName("game ended without deuce or advantage")
    void game_end_without_deuce_test(TennisGame startingScore, String scoringPlayer, TennisGame expectedScore)  {
        assertNotEquals(GameStatusEnum.ADVANTAGE_FIRST_PLAYER, expectedScore.gameStatus(), "shouldn't have advantage!");
        assertNotEquals(GameStatusEnum.ADVANTAGE_SECOND_PLAYER, expectedScore.gameStatus(), "shouldn't have advantage!");
        assertTrue(startingScore.firstPlayerScore() == ScoreEnum.FORTY || startingScore.secondPlayerScore() == ScoreEnum.FORTY,"one state of starting score must be " + ScoreEnum.FORTY);

        assertEquals(expectedScore, TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer));
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball | new score {2}")
    @ArgumentsSource(DeuceTennisGameData.class)
    @DisplayName("game move to deuce after advantage")
    void move_to_deuce_test(TennisGame startingScore, String scoringPlayer, TennisGame expectedScore) {
        TennisGame game = TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer);
        assertEquals(expectedScore, game);
        assertNotEquals(ScoreEnum.WIN, game.firstPlayerScore());
        assertNotEquals(ScoreEnum.WIN, game.firstPlayerScore());
        assertEquals(GameStatusEnum.DEUCE, game.gameStatus());
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball | new score {2}")
    @ArgumentsSource(AdvantageTennisGameData.class)
    @DisplayName("game move to advantage after deuce or forty/forty")
    void move_to_advantage_test(TennisGame startingScore, String scoringPlayer, TennisGame expectedScore) {
        TennisGame game = TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer);
        assertEquals(expectedScore, game);
        assertNotEquals(ScoreEnum.WIN, game.firstPlayerScore());
        assertNotEquals(ScoreEnum.WIN, game.firstPlayerScore());
        assertNotEquals(GameStatusEnum.FINISHED, game.gameStatus());
        assertNotEquals(GameStatusEnum.STARTED, game.gameStatus());
        assertNotEquals(GameStatusEnum.DEUCE, game.gameStatus());
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball | new score {2}")
    @ArgumentsSource(FinalScoreWithAdvantageData.class)
    @DisplayName("game finished after advantage status")
    void game_ended_after_advantage_test(TennisGame startingScore, String scoringPlayer, TennisGame expectedScore) {
        assertTrue(startingScore.gameStatus() == GameStatusEnum.ADVANTAGE_FIRST_PLAYER || startingScore.gameStatus() == GameStatusEnum.ADVANTAGE_SECOND_PLAYER, "game should be in advantage state");
        TennisGame game = TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer);
        assertEquals(expectedScore, game);
        assertTrue(TennisServiceImpl.getInstance().isGameFinished(game));

    }

    @ParameterizedTest(name = "score {0} | {1} won the ball -> illegal state")
    @ArgumentsSource(GameAlreadyFinishedData.class)
    @DisplayName("cannot play after game finished")
    void game_finished_test(TennisGame startingScore, String scoringPlayer) {
        GameAlreadyFinishedException gameAlreadyEndException = assertThrows(GameAlreadyFinishedException.class, () ->  TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer));
        assertNotNull(gameAlreadyEndException.getMessage(), "error message shouldn't be null");
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball -> illegal state")
    @ArgumentsSource(WrongPlayerNameData.class)
    @DisplayName("wrong player name")
    void wrong_player_name_test(TennisGame startingScore, String scoringPlayer) {
        PlayerNamingException gameAlreadyEndException = assertThrows(PlayerNamingException.class, () ->  TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer));
        assertNotNull(gameAlreadyEndException.getMessage(), "error message shouldn't be null");
        assertNotNull(gameAlreadyEndException.getPlayerName(), scoringPlayer);
    }

    @ParameterizedTest(name = "score {0} | {1} won the ball -> illegal state")
    @ArgumentsSource(IrrelevantCaseData.class)
    @DisplayName("irrelevant case for null player & game status")
    void irrelevant_case_test(TennisGame startingScore, String scoringPlayer) {
        assertThrows(GenericGameException.class, () ->  TennisServiceImpl.getInstance().playPoint(startingScore, scoringPlayer));
    }


    static class BallAfterSimpleScoreData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisServiceImpl.getInstance().startGame(), "A", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.ZERO, GameStatusEnum.STARTED)),
                    Arguments.of(TennisServiceImpl.getInstance().startGame(), "B", TennisGame.init(ScoreEnum.ZERO, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.ZERO, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.ZERO, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.ZERO, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.ZERO, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.ZERO, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.ZERO, ScoreEnum.THIRTY, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.ZERO, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.ZERO, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.ZERO, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.THIRTY, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.ZERO, ScoreEnum.THIRTY, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.THIRTY, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.ZERO, ScoreEnum.THIRTY, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.ZERO, ScoreEnum.FORTY, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.THIRTY, GameStatusEnum.STARTED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FORTY, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.FORTY, GameStatusEnum.STARTED))

            );
        }
    }

    static class FinalScoreWithoutDeuceData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.ZERO, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.ZERO, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.ZERO, ScoreEnum.FORTY, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.ZERO, ScoreEnum.WIN, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FIFTEEN, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.FORTY, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.WIN, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.THIRTY, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.THIRTY, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.FORTY, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.THIRTY, ScoreEnum.WIN, GameStatusEnum.FINISHED))
            );
        }
    }


    static class DeuceTennisGameData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_SECOND_PLAYER), "A", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.DEUCE)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_FIRST_PLAYER), "B", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.DEUCE))
            );
        }
    }


    static class AdvantageTennisGameData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.STARTED), "A", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_FIRST_PLAYER)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.STARTED), "B", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_SECOND_PLAYER)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.DEUCE), "A", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_FIRST_PLAYER)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.DEUCE), "B", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_SECOND_PLAYER))
            );
        }
    }

    static class FinalScoreWithAdvantageData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_FIRST_PLAYER), "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FORTY, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FORTY, ScoreEnum.FORTY, GameStatusEnum.ADVANTAGE_SECOND_PLAYER), "B", TennisGame.init(ScoreEnum.FORTY, ScoreEnum.WIN, GameStatusEnum.FINISHED))
            );
        }
    }

    static class GameAlreadyFinishedData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED), "B", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.WIN, GameStatusEnum.FINISHED), "A", TennisGame.init(ScoreEnum.FIFTEEN, ScoreEnum.WIN, GameStatusEnum.FINISHED))
            );
        }
    }
    static class WrongPlayerNameData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED), "C", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED))
            );
        }
    }
    static class IrrelevantCaseData implements ArgumentsProvider {
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(null, "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED)),
                    Arguments.of(null, "A", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED)),
                    Arguments.of(null, "B", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED)),
                    Arguments.of(TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, null), "B", TennisGame.init(ScoreEnum.WIN, ScoreEnum.FIFTEEN, GameStatusEnum.FINISHED))
            );
        }
    }
}
