package com.rps.engine;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.rps.model.Move;

public class RPSEngineRandomTest {
    @Test
    public void testRandomMove() {
        RPSEngineService sut = new RPSEngineService();

        Move random = sut.getRandomMove();
        assertNotNull("Should not return null for random move", random);
    }
}
