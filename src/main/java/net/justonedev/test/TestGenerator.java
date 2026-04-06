package net.justonedev.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestGenerator {
    private Random random;
    private List<ClosedInterval> allowedValues;
    private int boardWidth = -1;

    public void configureGenerator(List<ClosedInterval> allowedValues, int boardWidth, long seed) {
        this.allowedValues = new ArrayList<>(allowedValues);
        this.boardWidth = Math.max(2, boardWidth);
        this.random = new Random(seed);
    }

    public List<TestDatapoint> generateDatapoints(int size, HardcodedEvaluator evaluator) {
        if (boardWidth == -1 || allowedValues == null || allowedValues.isEmpty()) {
            throw new IllegalStateException("The Generator must be initialized before generating Datapoints.");
        }
        List<TestDatapoint> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(generateDataPoint(evaluator));
        }
        return list;
    }

    private TestDatapoint generateDataPoint(HardcodedEvaluator evaluator) {
        double[][] board = new double[boardWidth][boardWidth];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = generateDataValue();
            }
        }
        return new TestDatapoint(board, evaluator.evaluate(board));
    }

    private double generateDataValue() {

        ClosedInterval interval = allowedValues.get(random.nextInt(allowedValues.size()));
        return interval.getMinimum() + random.nextDouble() * (interval.getMaximum() - interval.getMinimum());
    }
}
