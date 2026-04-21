package net.justonedev.neural;

import lombok.AccessLevel;
import lombok.Getter;
import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.neural.initialization.HeType;
import net.justonedev.neural.initialization.Initializer;
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
        weights = new double[inputs];
        System.out.println("Neuron has " + inputs + " inputs. Bounds should be " + HeType.HE_UNIFORM.getIntervalBorder(inputs) + " Weights are:");
        for (int i = 0; i < inputs; i++) {
            weights[i] = initializer.nextDouble();
            System.out.println(weights[i]);
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
