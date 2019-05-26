package com.rps.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MovePaperTest {
    Move sut = Move.PAPER;

    @Test
    public void testPaperIsBetterThanPaper() {
        assertFalse("Paper should not be better than Paper", sut.isBetterThan(Move.PAPER));
    }

    @Test
    public void testPaperIsBetterThanScissors() {
        assertFalse("Paper should not be better than Scissors", sut.isBetterThan(Move.SCISSORS));
    }

    @Test
    public void testPaperIsBetterThanRock() {
        assertTrue("Paper should be better than Rock", sut.isBetterThan(Move.ROCK));
    }

    @Test
    public void testPaperIsBetterThanNull() {
        assertFalse("Paper should not be better than null", sut.isBetterThan(null));
    }

    @Test
    public void testGetPaperByName() {
        Move paper = Move.getByName("paper");
        assertEquals("Should be Paper", paper, Move.PAPER);
    }
}
