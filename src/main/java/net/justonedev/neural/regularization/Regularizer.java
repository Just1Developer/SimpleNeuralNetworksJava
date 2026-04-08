package net.justonedev.neural.regularization;

import lombok.Getter;

public abstract class Regularizer {
    @Getter
    private final double regularizingFactor;

    protected Regularizer(double regularizingFactor) {
        this.regularizingFactor = regularizingFactor;
    }

    public abstract double regularize(double[] weights);
}
