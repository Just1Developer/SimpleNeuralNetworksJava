package net.justonedev.neural.regularization;

public class NoRegularizer extends Regularizer {

    public NoRegularizer() {
        super(0);
    }

    @Override
    public double regularize(double[] weights) {
        return 0;
    }
}
