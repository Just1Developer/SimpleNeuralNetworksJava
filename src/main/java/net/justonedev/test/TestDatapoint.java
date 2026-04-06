package net.justonedev.test;

import net.justonedev.environment.Board;

public record TestDatapoint(Board board, double score) {
    public TestDatapoint(double[][] board, double score) {
        this(new Board(board), score);
    }
}
