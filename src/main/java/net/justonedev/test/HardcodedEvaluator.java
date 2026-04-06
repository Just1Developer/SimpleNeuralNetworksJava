package net.justonedev.test;

public interface HardcodedEvaluator {
    // A board is an 8x8 bitfield (here represented as byte[][], because we don't care about that memory that much here)
    double evaluate(double[][] board);
}
