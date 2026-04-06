package net.justonedev.neural;

import lombok.Setter;
import net.justonedev.neural.activation.ActivationFunction;

public class NeuronLayer {
    @Setter
    private ActivationFunction activationFunction;
    private final Initializer initializer;
    private final Neuron[] neurons;

    public NeuronLayer(int neurons, ActivationFunction activationFunction, Initializer initializer) {
        this.activationFunction = activationFunction;
        this.neurons = new Neuron[neurons];
        for (int i = 0; i < neurons; i++) {
            this.neurons[i] = new Neuron(0, activationFunction);
        }
        this.initializer = initializer;
    }

    public int getSize() {
        return neurons.length;
    }

    public void connect(NeuronLayer previousLayer) {
        int outputs = previousLayer.getSize();
        for (Neuron neuron : neurons) {
            neuron.initialize(outputs, initializer);
        }
    }

    public double[] forwardPass(double[] previousLayerState) {
        double[] neuronStates = new double[neurons.length];
        for (int n = 0; n < neurons.length; n++) {
            neuronStates[n] = neurons[n].calculateOutput(previousLayerState);
        }
        return neuronStates;
    }
}
