package net.justonedev.test;

import net.justonedev.environment.Board;

public record TestDatapoint(Board board, double[] target) {
    public TestDatapoint(double[][] board, double[] target) {
        this(new Board(board), target);
    }
    public TestDatapoint(double[][] board, double target) {
        this(new Board(board), new double[] { target });
    }
}
