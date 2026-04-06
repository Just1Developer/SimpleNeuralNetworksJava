package net.justonedev.neural.regularization;

public abstract class Regularizer {
    private final double regularizingFactor;

    protected Regularizer(double regularizingFactor) {
        this.regularizingFactor = regularizingFactor;
    }

    protected double getRegularizingFactor() {
        return regularizingFactor;
    }

    public abstract double regularize(double[] weights);
}
