package com.rps.model;

public class GameStatistics {
	int gamesNumber;
	int computerWin;
	int humanWin;
	int draw;
	
	public int getGamesNumber() {
		return gamesNumber;
	}
	public void setGamesNumber(int gamesNumber) {
		this.gamesNumber = gamesNumber;
	}
	public int getComputerWin() {
		return computerWin;
	}
	public void setComputerWin(int computerWin) {
		this.computerWin = computerWin;
	}
	public int getHumanWin() {
		return humanWin;
	}
	public void setHumanWin(int humanWin) {
		this.humanWin = humanWin;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
}
