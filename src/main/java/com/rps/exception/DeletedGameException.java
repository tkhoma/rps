package com.rps.exception;

public class DeletedGameException extends Exception {
    private static final long serialVersionUID = 1L;

    public DeletedGameException(long id) {
        super(String.format("The game with id %d is deleted!", id));
    }

}
