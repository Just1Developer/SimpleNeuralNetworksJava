package net.justonedev.test;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * This Evaluation is based on distance to the center, where the outermost ring is -1, the one after that is 0 and +0.5
 * for each even inner ring after that.
 */
public class RingDistanceEvaluator implements HardcodedEvaluator {
    private static final double OUTER_MOST_RING_SCORE = -1;
    private static final double STEP_SIZE = 0.5;
    private static final int ZERO_VALUED_RINGS = 1;
    private static final int ZERO_RING_SCORE = 0;

    @Override
    public double evaluate(double[][] board) {
        double eval = 0;
        int maxRing = getRing(board.length, 0, 0);
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                eval += getEvalForSingleValue(board, x, y, maxRing);
            }
        }
        return eval;
    }

    private static double getEvalForSingleValue(double[][] board, int x, int y, int maxRing) {
        double value = board[y][x];
        int ring = getRing(board.length, x, y);
        if (ring == maxRing) {
            return value * OUTER_MOST_RING_SCORE;
        } else if (maxRing - ring > ZERO_VALUED_RINGS) {
            return value * (maxRing - ring - ZERO_VALUED_RINGS) * STEP_SIZE;
        }
        return ZERO_RING_SCORE;
    }

    private static int getRing(int boardSize, int x, int y) {
        // Center: boardSize + 1
        // Offset: abs(center - 2(x+1)) / 2

        // Put in it reduces to:

        int offsetX = (int) Math.abs(boardSize / 2d - x - 0.5d);
        int offsetY = (int) Math.abs(boardSize / 2d - y - 0.5d);

        // + + + + + + + +
        // + 2 2 2 2 2 2 +
        // + 2 1 1 1 1 2 +
        // + 2 1 0 0 1 2 +
        // + 2 1 0 0 1 2 +
        // + 2 1 1 1 1 2 +
        // + 2 2 2 2 2 2 +
        // + + + + + + + +

        return Math.max(offsetX, offsetY);
    }

    // ------------------------------------- TEST METHODS -------------------------------------


    public static void main(String[] args) {
        testGetRing();
        testEvaluate(8);
        testEvaluate(10);
    }

    private static void testEvaluate(int size) {
        double[][] x = new double[size][size];
        for (int y = 0; y < size; y++) {
            Arrays.fill(x[y], 1.0);
        }

        System.out.printf("Size: %s%n", size);
        int maxRing = getRing(x.length, 0, 0);
        double[][] eval = new double[size][size];
        for (int y = 0; y < size; y++) {
            for (int i = 0; i < size; i++) {
                eval[y][i] = getEvalForSingleValue(x, i, y, maxRing);
            }
        }

        StringJoiner output = new StringJoiner(System.lineSeparator());
        appendArray(output, eval, "%1.1f");
        output.add("Total Eval: %f".formatted(new RingDistanceEvaluator().evaluate(x)));
        System.out.println(output);
    }

    private static void testGetRing() {
        printTestGetRing(1);
        printTestGetRing(2);
        printTestGetRing(3);
        printTestGetRing(4);
        printTestGetRing(5);
        printTestGetRing(6);
        printTestGetRing(7);
        printTestGetRing(8);
        printTestGetRing(9);
        printTestGetRing(10);
        printTestGetRing(16);
    }

    private static void printTestGetRing(int size) {
        double[][] x = new double[size][size];
        for (int y = 0; y < size; y++) {
            for (int i = 0; i < size; i++) {
                x[y][i] = getRing(size, i, y);
            }
        }

        //print

        System.out.printf("Size: %s%n", size);
        StringJoiner output = new StringJoiner(System.lineSeparator());
        appendArray(output, x, "%.0f");
        System.out.println(output);
    }

    private static void appendArray(StringJoiner output, double[][] board, String formatString) {
        for (double[] row : board) {
            StringJoiner line = new StringJoiner(" ");
            for (double field : row) {
                line.add(formatString.formatted(field));
            }
            output.add(line.toString());
        }
    }
}
