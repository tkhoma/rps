package com.rps.engine;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.rps.model.Move;
import com.rps.model.Result;

@RunWith(Parameterized.class)
public class RPSEnginePositiveTest {

    @Parameters(name ="{index}: {0} vs. {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Move.ROCK, Move.ROCK, Result.DRAW },
            { Move.ROCK, Move.PAPER, Result.LOOSE },
            { Move.ROCK, Move.SCISSORS, Result.WIN },
            { Move.PAPER, Move.ROCK, Result.WIN },
            { Move.PAPER, Move.PAPER, Result.DRAW },
            { Move.PAPER, Move.SCISSORS, Result.LOOSE },
            { Move.SCISSORS, Move.ROCK, Result.LOOSE },
            { Move.SCISSORS, Move.PAPER, Result.WIN },
            { Move.SCISSORS, Move.SCISSORS, Result.DRAW }
        });
    }

    private Move playerOneMove;
    private Move playerTwoMove;
    private Result expected;

    public RPSEnginePositiveTest(Move playerOneMove, Move playerTwoMove, Result expected) {
        this.playerOneMove = playerOneMove;
        this.playerTwoMove = playerTwoMove;
        this.expected = expected;
    }

    @Test
    public void testRockPaperScissors() {
        RPSEngineService sut = new RPSEngineService();
        Result actual = sut.evaluateMoves(playerOneMove, playerTwoMove);

        assertEquals(expected, actual);
    }
}
