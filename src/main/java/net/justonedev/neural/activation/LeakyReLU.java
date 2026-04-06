package net.justonedev.neural.activation;

public class LeakyReLU implements ActivationFunction {
    private final double alphaValue;

    public LeakyReLU(double alphaValue) {
        this.alphaValue = alphaValue;
    }

    @Override
    public double activate(double value) {
        return value < 0 ? alphaValue * value : value;
    }
}
