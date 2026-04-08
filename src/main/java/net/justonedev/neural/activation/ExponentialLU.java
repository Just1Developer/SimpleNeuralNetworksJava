package net.justonedev.neural.activation;

public class ExponentialLU implements ActivationFunction {
    private final double alphaValue;

    public ExponentialLU(double alphaValue) {
        this.alphaValue = alphaValue;
    }

    @Override
    public double activate(double value) {
        return value < 0 ? alphaValue * (Math.exp(value) - 1) : 1;
    }

    @Override
    public double derivative(double value) {
        return value < 0 ? alphaValue * (Math.exp(value)) : value == 0 ? 0 : 1;
    }
}
