package net.justonedev.neural;

import lombok.Setter;
import net.justonedev.neural.activation.ActivationFunction;

public class NeuronLayer {
    @Setter
    private ActivationFunction activationFunction;
    private double[][] weightMatrix;

    public NeuronLayer(int neurons, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public void connect(NeuronLayer nextLayer) {

    }
}
