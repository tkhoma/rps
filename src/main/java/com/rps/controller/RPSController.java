package com.rps.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rps.engine.RPSEngineService;
import com.rps.exception.DeletedGameException;
import com.rps.exception.FinishedGameException;
import com.rps.exception.GameDoesNotExistsException;
import com.rps.model.Game;
import com.rps.model.GameStatistics;
import com.rps.model.GameStatus;
import com.rps.model.Move;
import com.rps.model.Result;
import com.rps.repository.GameRepository;

@RestController
@RequestMapping(value = "/games")
public class RPSController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RPSEngineService rpsEngine;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpHeaders startNewGame() {
        Game game = new Game();
        gameRepository.save(game);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(RPSController.class)
            .slash(game.getId()).toUri());

        return headers;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Optional<Game> getGame(@PathVariable long id) throws GameDoesNotExistsException {
        Optional<Game> game = gameRepository.findById(id);

        if (!game.isPresent()) {
            throw new GameDoesNotExistsException(id);
        }

        return game;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGame(@PathVariable long id) throws GameDoesNotExistsException, DeletedGameException {
        Optional<Game> game = gameRepository.findById(id);

        if (!game.isPresent()) {
            throw new GameDoesNotExistsException(id);
        }

        Game foundGame = game.get();

        if (foundGame.getStatus() == GameStatus.DELETED) {
            throw new DeletedGameException(id);
        }

        foundGame.setStatus(GameStatus.DELETED);

        gameRepository.save(foundGame);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void play(@PathVariable long id, @RequestBody String humanMoveString) throws GameDoesNotExistsException, DeletedGameException, FinishedGameException {
    	Move humanPlayerMove = Move.getByName(humanMoveString);
        Optional<Game> game = gameRepository.findById(id);

        if (!game.isPresent()) {
            throw new GameDoesNotExistsException(id);
        }

        Game foundGame = game.get();

        if (foundGame.getStatus() == GameStatus.DELETED) {
            throw new DeletedGameException(id);
        }

        if (foundGame.getStatus() == GameStatus.FINISHED) {
            throw new FinishedGameException(id);
        }


        Move computerPlayerMove = rpsEngine.getRandomMove();
        Result result = rpsEngine.evaluateMoves(humanPlayerMove, computerPlayerMove);

        foundGame.setHumanPlayerMove(humanPlayerMove);
        foundGame.setComputerPlayerMove(computerPlayerMove);
        foundGame.setResult(result);
        foundGame.setStatus(GameStatus.FINISHED);

        gameRepository.save(foundGame);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/statistics")
    @ResponseStatus(HttpStatus.OK)
    public GameStatistics statistics() {
    	Iterable<Game> allGames = gameRepository.findAll();
    	
    	GameStatistics statistics = new GameStatistics();
    	int gamesCount = 0;
    	int humanWin = 0;
    	int computerWin = 0;
    	int draw = 0;
    	for (Game game : allGames) {
			gamesCount++;
			
			switch(game.getResult()) {
				case DRAW:
					draw++;
					break;
				case LOOSE:
					computerWin++;
					break;
				case WIN:
					humanWin++;
					break;
			}
		}
    	
    	statistics.setHumanWin(humanWin);
    	statistics.setComputerWin(computerWin);
    	statistics.setDraw(draw);
    	statistics.setGamesNumber(gamesCount);
    	return statistics;
    }
}
