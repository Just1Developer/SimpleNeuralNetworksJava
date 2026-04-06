package net.justonedev.environment;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Board {
    private final double[][] board;
    private final double[] raw;
    private final int sidelength;

    public Board(double[][] board) {
        this.sidelength = board.length;
        this.board = Arrays.copyOf(board, board.length);
        this.raw = new double[board.length * board[0].length];
        for (int i = 0, y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; i++, x++) {
                this.raw[i] = board[y][x];
            }
        }
    }
}
