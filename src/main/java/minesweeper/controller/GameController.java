package minesweeper.controller;

import jakarta.validation.Valid;
import minesweeper.dto.GameInfoResponse;
import minesweeper.dto.GameTurnRequest;
import minesweeper.dto.NewGameRequest;
import minesweeper.service.GameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public GameInfoResponse newGame(@Valid @RequestBody NewGameRequest request) {
        return gameService.newGame(request);
    }

    @PostMapping("/turn")
    public GameInfoResponse turn(@RequestBody GameTurnRequest request) {
        return gameService.turn(request);
    }
}
