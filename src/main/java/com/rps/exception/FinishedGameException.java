package com.rps.exception;

public class FinishedGameException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FinishedGameException(long id) {
		super(String.format("The game with id %d is finished!", id));
	}
}
