package net.justonedev.neural;

import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.test.ClosedInterval;

public class Neuron {
    private double[] weights;
    private final double bias;
    private final ActivationFunction activationFunction;

    public Neuron(double bias, ActivationFunction activationFunction) {
        this.bias = bias;
        this.activationFunction = activationFunction;
    }

    public void initialize(int outputs, Initializer initializer) {
        this.initialize(outputs, initializer, null);
    }

    public void initialize(int outputs, Initializer initializer, ClosedInterval valueRange) {
        weights = new double[outputs];
        for (int i = 0; i < outputs; i++) {
            weights[i] = initializer.nextDouble(valueRange);
        }
    }

    public double calculateOutput(double[] inputs) {
        if (inputs.length != weights.length) {
            throw new IllegalStateException("Neuron received number of inputs not equal to the number of weights.");
        }
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return activationFunction.activate(sum);
    }
}
