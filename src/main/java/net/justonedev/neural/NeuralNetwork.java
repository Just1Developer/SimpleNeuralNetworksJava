package net.justonedev.neural;

import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.neural.regularization.Regularizer;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private final NeuronLayer inputLayer;
    private final List<NeuronLayer> hiddenLayers;
    private final NeuronLayer outputLayer;

    private final Regularizer regularizer;

    public NeuralNetwork(int inputs, int outputs, int hiddenLayers, int hiddenLayerSize, ActivationFunction activationFunction, Regularizer regularizer) {
        this.inputLayer = new NeuronLayer(inputs, activationFunction);
        this.hiddenLayers = new ArrayList<>(hiddenLayers);
        for (int i = 0; i < hiddenLayers; i++) {
            this.hiddenLayers.add(new NeuronLayer(hiddenLayerSize, activationFunction));
        }
        this.outputLayer = new NeuronLayer(outputs, activationFunction);

        this.regularizer = regularizer;
    }

}
