package com.rps.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Game {
    @Id
    @GeneratedValue
    private long id;

    private GameStatus status;
    private Move humanPlayerMove;
    private Move computerPlayerMove;
    private Result result;

    public Game() {
        this.status = GameStatus.STARTED;
    }

    public long getId() {
        return id;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setHumanPlayerMove(Move humanPlayerMove) {
        this.humanPlayerMove = humanPlayerMove;
    }

    public Move getHumanPlayerMove() {
        return humanPlayerMove;
    }

    public void setComputerPlayerMove(Move computerPlayerMove) {
        this.computerPlayerMove = computerPlayerMove;
    }

    public Move getComputerPlayerMove() {
        return computerPlayerMove;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
