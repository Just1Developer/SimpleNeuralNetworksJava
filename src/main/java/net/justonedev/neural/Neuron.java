package net.justonedev.neural;

import lombok.AccessLevel;
import lombok.Getter;
import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.test.ClosedInterval;

public class Neuron {
    @Getter(AccessLevel.PACKAGE)
    private double[] weights;
    private final double bias;
    private final ActivationFunction activationFunction;

    public Neuron(double bias, ActivationFunction activationFunction) {
        this.bias = bias;
        this.activationFunction = activationFunction;
    }

    public void initialize(int inputs, Initializer initializer) {
        this.initialize(inputs, initializer, null);
    }

    public void initialize(int inputs, Initializer initializer, ClosedInterval valueRange) {
        weights = new double[inputs];
        for (int i = 0; i < inputs; i++) {
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

    public void adjustWeights(double[] previousLayerNeuronValues, double delta, double learningRate, double weightDecayFactor) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * delta * previousLayerNeuronValues[i];
            weights[i] *= (1 - learningRate * weightDecayFactor);
        }
    }

    // ----------------------------- DEBUG -----------------------------

    public void debug_setWeights(double[] weights) {
        this.weights = weights;
    }
}
