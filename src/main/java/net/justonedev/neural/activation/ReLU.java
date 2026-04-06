package net.justonedev.neural.activation;

public class ReLU implements ActivationFunction {
    @Override
    public double activate(double value) {
        return value < 0 ? 0 : value;
    }
}
