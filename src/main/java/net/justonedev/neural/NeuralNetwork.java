package net.justonedev.neural;

import lombok.AccessLevel;
import lombok.Getter;
import net.justonedev.neural.activation.ActivationFunction;
import net.justonedev.neural.activation.ReLU;
import net.justonedev.neural.regularization.Regularizer;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    @Getter
    private final NeuronLayer inputLayer;
    @Getter
    private final List<NeuronLayer> hiddenLayers;
    @Getter
    private final NeuronLayer outputLayer;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final Regularizer regularizer;
    @Getter(AccessLevel.PACKAGE)
    private final double weightDecayFactor;

    @Getter
    private final double learningRate;

    public NeuralNetwork(int inputs, int outputs, int hiddenLayers, int hiddenLayerSize,
                         ActivationFunction activationFunction, Regularizer regularizer,
                         int initializerSeed, double learningRate) {
        this.inputLayer = new NeuronLayer(this, inputs, new ReLU(), initializerSeed); // Doesn't matter, inputLayer is just a container
        this.hiddenLayers = new ArrayList<>(hiddenLayers);
        for (int i = 0; i < hiddenLayers; i++) {
            this.hiddenLayers.add(new NeuronLayer(this, hiddenLayerSize, activationFunction, initializerSeed + 1));
            if (i == 0) {
                this.hiddenLayers.getFirst().connect(inputLayer);
            } else {
                this.hiddenLayers.get(i).connect(this.hiddenLayers.get(i - 1));
            }
        }
        this.outputLayer = new NeuronLayer(this, outputs, activationFunction, initializerSeed - 1);
        this.outputLayer.connect(hiddenLayers == 0 ? inputLayer : this.hiddenLayers.getLast());

        this.regularizer = regularizer;
        this.weightDecayFactor = regularizer.getRegularizingFactor();
        this.learningRate = learningRate;
    }

    public double[] think(double[] input) {
        double[] currentData = trimToFit(input, inputLayer.getSize());
        inputLayer.setNeuronData(currentData);
        for (NeuronLayer layer : hiddenLayers) {
            currentData = layer.forwardPass(currentData);
        }
        return outputLayer.forwardPass(currentData);
    }

    private static double[] trimToFit(double[] input, int size) {
        double[] result = new double[size];
        System.arraycopy(input, 0, result, 0, Math.min(size, input.length));
        return result;
    }

}
