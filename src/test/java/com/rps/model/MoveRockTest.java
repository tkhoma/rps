package com.rps.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveRockTest {
    Move sut = Move.ROCK;

    @Test
    public void testRockIsBetterThanPaper() {
        assertFalse("Rock should not be better than Paper", sut.isBetterThan(Move.PAPER));
    }

    @Test
    public void testRockIsBetterThanScissors() {
        assertTrue("Rock should be better than Scissors", sut.isBetterThan(Move.SCISSORS));
    }

    @Test
    public void testRockIsBetterThanRock() {
        assertFalse("Rock should not be better than Rock", sut.isBetterThan(Move.ROCK));
    }

    @Test
    public void testRockIsBetterThanNull() {
        assertFalse("Rock should not be better than null", sut.isBetterThan(null));
    }

    @Test
    public void testGetRockByName() {
        Move rock = Move.getByName("rock");
        assertEquals("Should be Rock", rock, Move.ROCK);
    }
}
