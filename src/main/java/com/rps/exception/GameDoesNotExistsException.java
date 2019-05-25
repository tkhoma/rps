package com.rps.exception;

public class GameDoesNotExistsException extends Exception {
	private static final long serialVersionUID = 1L;

    public GameDoesNotExistsException(long id) {
        super(String.format("The game with id %d does not exist!", id));
    }
}
