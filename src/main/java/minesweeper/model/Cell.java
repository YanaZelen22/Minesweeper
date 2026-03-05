package minesweeper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {

    @Setter
    private boolean mine;
    private boolean open;
    private boolean exploded;
    @Setter
    private int minesAround;

    public Cell(boolean mine, boolean open, int minesAround) {
        this.mine = mine;
        this.open = open;
        this.minesAround = minesAround;
    }

    public void open() { this.open = true; }

    public void explode() { this.exploded = true; }

}