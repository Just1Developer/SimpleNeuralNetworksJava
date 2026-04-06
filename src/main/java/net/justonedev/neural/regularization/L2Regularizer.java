package net.justonedev.neural.regularization;

public class L2Regularizer extends Regularizer {

    public L2Regularizer(double regularizingFactor) {
        super(regularizingFactor);
    }

    @Override
    public double regularize(double[] weights) {
        double dotProduct = 0;
        for (double weight : weights) {
            dotProduct += weight * weight;
        }
        return dotProduct * getRegularizingFactor();
    }
}
