package com.rps.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveScissorsTest {
    Move sut = Move.SCISSORS;

    @Test
    public void testScissorsIsBetterThanPaper() {
        assertTrue("Scissors should be better than Paper", sut.isBetterThan(Move.PAPER));
    }

    @Test
    public void testScissorsIsBetterThanScissors() {
        assertFalse("Scissors should not be better than Scissors", sut.isBetterThan(Move.SCISSORS));
    }

    @Test
    public void testScissorsIsBetterThanRock() {
        assertFalse("Rock should not be better than Rock", sut.isBetterThan(Move.ROCK));
    }

    @Test
    public void testScissorsIsBetterThanNull() {
        assertFalse("Rock should not be better than null", sut.isBetterThan(null));
    }

    @Test
    public void testGetScissorsByName() {
        Move scissors = Move.getByName("scissors");
        assertEquals("Should be Scissors", scissors, Move.SCISSORS);
    }
}
