package minesweeper.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Game {

    private final UUID id;
    private final int width;
    private final int height;
    private final int minesCount;

    private final boolean[][] mines;
    private final boolean[][] open;
    private final int[][] numbers;

    private boolean completed;

    public Game(UUID id, int width, int height, int minesCount) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;

        this.mines = new boolean[height][width];
        this.open = new boolean[height][width];
        this.numbers = new int[height][width];
    }

    public Cell[][] getBoard() {
        Cell[][] board = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = new Cell(mines[y][x], open[y][x], numbers[y][x]);
            }
        }
        return board;
    }

    public void complete() { completed = true; }
}