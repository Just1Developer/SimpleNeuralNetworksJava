package net.justonedev.neural;

import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.neural.activation.ReLU;
import net.justonedev.neural.regularization.Regularizer;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private final List<NeuronLayer> hiddenLayers;
    private final NeuronLayer outputLayer;

    private final Regularizer regularizer;

    public NeuralNetwork(int inputs, int outputs, int hiddenLayers, int hiddenLayerSize, ActivationFunction activationFunction, Regularizer regularizer, int initializerSeed) {
        Initializer initializer = new Initializer(initializerSeed);
        NeuronLayer inputLayer = new NeuronLayer(inputs, new ReLU(), initializer); // Doesn't matter, inputLayer is just a container
        this.hiddenLayers = new ArrayList<>(hiddenLayers);
        for (int i = 0; i < hiddenLayers; i++) {
            this.hiddenLayers.add(new NeuronLayer(hiddenLayerSize, activationFunction, initializer));
            if (i == 0) {
                this.hiddenLayers.getFirst().connect(inputLayer);
            } else {
                this.hiddenLayers.get(i).connect(this.hiddenLayers.get(i - 1));
            }
        }
        this.outputLayer = new NeuronLayer(outputs, activationFunction, initializer);
        this.outputLayer.connect(hiddenLayers == 0 ? inputLayer : this.hiddenLayers.getLast());

        this.regularizer = regularizer;
    }

    public double[] think(double[] input) {
        double[] currentData = input;
        for (NeuronLayer layer : hiddenLayers) {
            currentData = layer.forwardPass(currentData);
        }
        return outputLayer.forwardPass(currentData);
    }

}
