package net.justonedev.neural.regularization;

public class L1Regularizer extends Regularizer {

    public L1Regularizer(double regularizingFactor) {
        super(regularizingFactor);
    }

    @Override
    public double regularize(double[] weights) {
        double sum = 0;
        for (double weight : weights) {
            sum += weight;
        }
        return sum * getRegularizingFactor();
    }
}
