package minesweeper.service;

import minesweeper.dto.GameInfoResponse;
import minesweeper.dto.GameTurnRequest;
import minesweeper.dto.NewGameRequest;
import minesweeper.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    public final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public GameInfoResponse newGame(NewGameRequest request) {

        if (request.minesCount() > request.width() * request.height() - 1) {
            throw new IllegalArgumentException("Too many mines");
        }

        Game game = createGame(request);

        games.put(game.getId(), game);

        return mapToResponse(game);
    }

    public GameInfoResponse turn(GameTurnRequest request) {
        Game game = games.get(request.gameId());
        if (game == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found");

        if (game.isCompleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game already finished");

        int row = request.row();
        int col = request.col();

        if (row < 0 || row >= game.getHeight() || col < 0 || col >= game.getWidth())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cell coordinates out of bounds");

        if (game.getOpen()[row][col])
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cell already opened");

        if (game.getMines()[row][col]) {
            game.getOpen()[row][col] = true;
            game.complete();
            return mapToResponse(game, true);
        }

        open(game, col, row);

        if (checkWin(game)) {
            game.complete();
            return mapToResponse(game, false); // победа
        }

        return mapToResponse(game);
    }

    private Game createGame(NewGameRequest request) {
        Game game = new Game(
                UUID.randomUUID(),
                request.width(),
                request.height(),
                request.minesCount()
        );
        placeMines(game);
        calculateNumbers(game);
        return game;
    }

    private void placeMines(Game game) {
        Random random = new Random();
        int placed = 0;
        while (placed < game.getMinesCount()) {
            int x = random.nextInt(game.getWidth());
            int y = random.nextInt(game.getHeight());
            if (!game.getMines()[y][x]) {
                game.getMines()[y][x] = true;
                placed++;
            }
        }
    }

    private void calculateNumbers(Game game) {
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                if (game.getMines()[y][x]) continue;
                int count = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int ny = y + dy, nx = x + dx;
                        if (nx < 0 || ny < 0 || nx >= game.getWidth() || ny >= game.getHeight())
                            continue;
                        if (game.getMines()[ny][nx]) count++;
                    }
                }
                game.getNumbers()[y][x] = count;
            }
        }
    }

    private void open(Game game, int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int cx = pos[0], cy = pos[1];
            if (game.getOpen()[cy][cx]) continue;

            game.getOpen()[cy][cx] = true;

            if (game.getNumbers()[cy][cx] != 0) continue;

            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int nx = cx + dx, ny = cy + dy;
                    if (nx < 0 || ny < 0 || nx >= game.getWidth() || ny >= game.getHeight())
                        continue;
                    if (!game.getOpen()[ny][nx])
                        queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    private boolean checkWin(Game game) {
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                if (!game.getMines()[y][x] && !game.getOpen()[y][x])
                    return false;
            }
        }
        return true;
    }

    private GameInfoResponse mapToResponse(Game game, boolean... lostGame) {
        boolean lost = lostGame.length > 0 && lostGame[0];
        boolean won = game.isCompleted() && !lost;

        String[][] field = new String[game.getHeight()][game.getWidth()];
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                if (!game.getOpen()[y][x]) {
                    field[y][x] = " ";
                } else if (game.getMines()[y][x]) {
                    field[y][x] = lost ? "X" : (won ? "M" : "X"); // проигрыш: X, победа: M
                } else {
                    field[y][x] = String.valueOf(game.getNumbers()[y][x]);
                }
            }
        }

        if (game.isCompleted()) {
            for (int y = 0; y < game.getHeight(); y++) {
                for (int x = 0; x < game.getWidth(); x++) {
                    if (!game.getOpen()[y][x]) {
                        game.getOpen()[y][x] = true;
                        field[y][x] = game.getMines()[y][x] ? (lost ? "X" : "M") : String.valueOf(game.getNumbers()[y][x]);
                    }
                }
            }
        }

        return new GameInfoResponse(
                game.getId(),
                game.getWidth(),
                game.getHeight(),
                game.getMinesCount(),
                game.isCompleted(),
                field
        );
    }

    private GameInfoResponse mapToResponse(Game game) {
        return mapToResponse(game, false);
    }
}
