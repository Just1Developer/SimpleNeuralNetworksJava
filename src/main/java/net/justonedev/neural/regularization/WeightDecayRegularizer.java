package net.justonedev.neural.regularization;

public class WeightDecayRegularizer extends Regularizer {

    public WeightDecayRegularizer(double regularizingFactor) {
        super(regularizingFactor);
    }

    @Override
    public double regularize(double[] weights) {
        return 0;
    }
}
