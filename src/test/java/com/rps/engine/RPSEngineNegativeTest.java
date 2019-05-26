package com.rps.engine;

import org.junit.Before;
import org.junit.Test;

import com.rps.model.Move;

public class RPSEngineNegativeTest {
    private RPSEngineService sut;

    @Before
    public void setUp() throws Exception {
        sut = new RPSEngineService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBothMovesAreNull() {
        sut.evaluateMoves(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFirstMoveIsNull() {
        sut.evaluateMoves(null, Move.ROCK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecondMoveIsNull() {
        sut.evaluateMoves(Move.ROCK, null);
    }
}
