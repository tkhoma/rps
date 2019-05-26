package com.rps.model;

import java.util.HashMap;
import java.util.Map;

public enum Move {
    ROCK {
        @Override
        public boolean isBetterThan(Move move) {
            return SCISSORS == move;
        }
    }, PAPER {
        @Override
        public boolean isBetterThan(Move move) {
            return ROCK == move;
        }
    }, SCISSORS {
        @Override
        public boolean isBetterThan(Move move) {
            return PAPER == move;
        }
    };

    public abstract boolean isBetterThan(Move computerPlayerMove);
    public static Map<String, Move> movesByName = new HashMap<>();

    static {
        Move[] moves = Move.values();
        for (Move move : moves) {
            movesByName.put(move.name(), move);
        }
    }

    public static Move getByName(String name) {
        return movesByName.get(name.toUpperCase());
    }
}
