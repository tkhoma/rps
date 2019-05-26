package com.rps.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rps.Application;
import com.rps.model.Game;
import com.rps.model.GameStatistics;
import com.rps.model.GameStatus;
import com.rps.model.Move;
import com.rps.model.Result;
import com.rps.repository.GameRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RPSControllerTest {
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private GameRepository gameRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() {
        gameRepository.deleteAll();
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testCreateNewGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        assertNotNull("uri should contain the location of the new game!", uri);

        ResponseEntity<Game> entity = restTemplate.getForEntity(uri, Game.class);
        assertEquals("The request to the new game shold return status 200", HttpStatus.OK, entity.getStatusCode());

        Game game = entity.getBody();
        assertNotNull("The response body should contain the new game", game);
        assertEquals("The status of the game should 'STARTED'", GameStatus.STARTED, game.getStatus());
        assertNull("There should be no move for human player", game.getHumanPlayerMove());
        assertNull("There should be no move for computer player", game.getComputerPlayerMove());
        assertNull("There should be no result", game.getResult());
    }

    @Test
    public void testAbortGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        restTemplate.delete(uri);

        ResponseEntity<Game> entity = restTemplate.getForEntity(uri, Game.class);
        Game game = entity.getBody();

        assertEquals("The status of the game should be 'deleted'", GameStatus.DELETED, game.getStatus());
    }

    @Test
    public void testPlayGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(Move.ROCK.name() ,headers);
        restTemplate.put(uri, requestEntity);

        ResponseEntity<Game> entity = restTemplate.getForEntity(uri, Game.class);
        Game game = entity.getBody();

        assertNotNull("The game must not be null", game);
        assertEquals("The status of the game should be 'FINISHED'", GameStatus.FINISHED, game.getStatus());
        assertNotNull("There should be a move for human player", game.getHumanPlayerMove());
        assertEquals("The move of human player should be 'ROCK'", Move.ROCK, game.getHumanPlayerMove());
        assertNotNull("There should be a move for computer player", game.getComputerPlayerMove());
        assertNotNull("There should be a result", game.getResult());
    }

    @Test
    public void testPlayAnInvalidMove() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> request = new HttpEntity<>(42, headers);

        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
        assertEquals("This should be a 'Not Found (404)'", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPlayNullAsMove() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
        assertEquals("This should be a 'Bad Request (400)'", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetGameWithWrongId() {
        ResponseEntity<Void> response = restTemplate.getForEntity(getBaseUrl() + "/games/1", Void.class);
        assertEquals("There is no entity, so the result should be 404", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetGameWithIdInWrongFormat() {
        ResponseEntity<Void> response = restTemplate.getForEntity(getBaseUrl() + "/games/one", Void.class);
        assertEquals("The format of the id is wrong, so the result should be 400", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAbortNonExistingGame() {
        ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/games/1", HttpMethod.DELETE, null, Void.class);
        assertEquals("This game does not exist, so 404 is expected", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPlayNonExistingGame() throws Exception {
        URI uri = new URI(getBaseUrl() + "/games/1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(Move.ROCK.name() ,headers);
        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Void.class);
        assertEquals("The game does not exist, so the result should be 404", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFinishFinishedGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        restTemplate.delete(uri);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals("This game is already finished, so it can't be finished twice", HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testDeleteFinishedGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(Move.ROCK.name() ,headers);
        restTemplate.put(uri, requestEntity);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals("The game is already finished, so it can't be deleted", HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testPlayDeletedGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        restTemplate.delete(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Move> request = new HttpEntity<>(Move.ROCK, headers);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
        assertEquals("The game is already deleted, making a move is not allowed", HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testPlayFinishedGame() {
        URI uri = restTemplate.postForLocation(getBaseUrl() + "/games", null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(Move.ROCK.name() ,headers);
        restTemplate.put(uri, requestEntity);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Void.class);
        assertEquals("The game is already finished, making a second move is not allowed", HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void testStatistics() {
    	Game winGame = new Game();
    	winGame.setComputerPlayerMove(Move.ROCK);
    	winGame.setHumanPlayerMove(Move.PAPER);
    	winGame.setStatus(GameStatus.FINISHED);
    	winGame.setResult(Result.WIN);
    	gameRepository.save(winGame);
    	
    	Game lostGame = new Game();
    	lostGame.setComputerPlayerMove(Move.SCISSORS);
    	lostGame.setHumanPlayerMove(Move.PAPER);
    	lostGame.setStatus(GameStatus.FINISHED);
    	lostGame.setResult(Result.LOOSE);
    	gameRepository.save(lostGame);
    	
    	Game drawGame = new Game();
    	drawGame.setComputerPlayerMove(Move.ROCK);
    	drawGame.setHumanPlayerMove(Move.ROCK);
    	drawGame.setStatus(GameStatus.FINISHED);
    	drawGame.setResult(Result.DRAW);
    	gameRepository.save(drawGame);
    	
    	ResponseEntity<GameStatistics> statistics = restTemplate.getForEntity(getBaseUrl() + "/games/statistics", GameStatistics.class);
    	assertEquals("There should be one win game", statistics.getBody().getHumanWin(), 1);
    	assertEquals("There should be one lost game", statistics.getBody().getComputerWin(), 1);
    	assertEquals("There should be one draw game", statistics.getBody().getDraw(), 1);
    	assertEquals("There should be 3 games", statistics.getBody().getGamesNumber(), 3);
    }
}
