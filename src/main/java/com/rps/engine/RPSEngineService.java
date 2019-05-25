package com.rps.engine;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.rps.model.Move;
import com.rps.model.Result;

@Service
public class RPSEngineService {
    private static final Move[] moves = Move.values();
    private static final Random random = new Random();

    public Move getRandomMove() {
        return moves[random.nextInt(moves.length)];
    }

    public Result evaluateMoves(Move humanPlayerMove, Move computerPlayerMove) {
        if (humanPlayerMove == null) {
            throw new IllegalArgumentException("Human Player made invalid move!");
        }

        if (computerPlayerMove == null) {
            throw new IllegalArgumentException("Computer Player made invalid move!");
        }

        if (humanPlayerMove.isBetterThan(computerPlayerMove)) {
            return Result.WIN;
        }

        if (computerPlayerMove.isBetterThan(humanPlayerMove)) {
            return Result.LOOSE;
        }

        return Result.DRAW;
    }

}
