package minesweeper.controller;

import jakarta.validation.Valid;
import minesweeper.dto.GameInfoResponse;
import minesweeper.dto.GameTurnRequest;
import minesweeper.dto.NewGameRequest;
import minesweeper.service.GameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public GameInfoResponse newGame(@Valid @RequestBody NewGameRequest request) {
        logger.debug("New game request: {}", request);

        return gameService.newGame(request);
    }

    @PostMapping("/turn")
    public GameInfoResponse turn(@RequestBody GameTurnRequest request) {
        logger.debug("Turn request: {}", request);

        return gameService.turn(request);
    }
}
