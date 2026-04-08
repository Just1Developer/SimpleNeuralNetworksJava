package net.justonedev.neural;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.justonedev.DebugMiniExample;
import net.justonedev.neural.activation.ActivationFunction;

import java.util.Arrays;

public class NeuronLayer {
    @Setter(AccessLevel.PACKAGE)
    private ActivationFunction activationFunction;
    private final Initializer initializer;
    @Getter // public for canvas
    private final Neuron[] neurons;
    private double[] neuronStates;
    private NeuronLayer previousLayer;
    private final NeuralNetwork network;

    public NeuronLayer(NeuralNetwork network, int neurons, ActivationFunction activationFunction, Initializer initializer) {
        this.network = network;
        this.activationFunction = activationFunction;
        this.neurons = new Neuron[neurons];
        for (int i = 0; i < neurons; i++) {
            this.neurons[i] = new Neuron(0, activationFunction);
        }
        this.initializer = initializer;
        neuronStates = new double[neurons];
    }

    public int getSize() {
        return neurons.length;
    }

    public void connect(NeuronLayer previousLayer) {
        this.previousLayer = previousLayer;
        int outputs = previousLayer.getSize();
        for (Neuron neuron : neurons) {
            neuron.initialize(outputs, initializer);
        }
    }

    public double[] forwardPass(double[] previousLayerState) {
        for (int n = 0; n < neurons.length; n++) {
            neuronStates[n] = neurons[n].calculateOutput(previousLayerState);
        }
        return neuronStates;
    }

    // returns delta[]
    public double[] calculateBackpropagationDeltas(double[] deltasOfNextLayer, NeuronLayer forwardLayer) {
        double[] deltas = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            double error = 0;
            double[] weights;
            for (int j = 0; j < forwardLayer.neurons.length; j++) {
                weights = forwardLayer.neurons[j].getWeights();
                error += weights[i] * deltasOfNextLayer[j];
            }
            error *= activationFunction.derivative(neuronStates[i]);
            deltas[i] = error;
        }
        return deltas;
    }

    public void adjustWeights(double[] deltas) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].adjustWeights(previousLayer.neuronStates, deltas[i], network.getLearningRate(), network.getWeightDecayFactor());
        }
    }

    /**
     * Calculates the delta from the expected values to the output layer.
     * This method should only be called on the output layer, not on the
     * hidden layers.<br/>
     * Use {@link NeuronLayer#calculateBackpropagationDeltas(double[], NeuronLayer)} for hidden layers.
     *
     * @param expectedValues the expected values for the test data. Size must match output size.
     * @return the delta values for the output layer.
     */
    public double[] calculateOutputDelta(double[] expectedValues) {
        double[] delta = new double[neurons.length];
        for (int i = 0; i < delta.length; i++) {
            delta[i] = (expectedValues[i] - neuronStates[i]) * activationFunction.derivative(neuronStates[i]);
        }
        return delta;
    }

    public void setNeuronData(double[] neuronData) {
        if (neuronData.length != neuronStates.length) {
            throw new IllegalArgumentException("Neuron Data Length Mismatch: Expected Neuron Data of size %d, but got size %d".formatted(neuronStates.length, neuronData.length));
        }
        this.neuronStates = Arrays.copyOf(neuronData, neuronStates.length);
    }

    /**
     * Begins the backpropagation and weight adjustment process. Should only be called on
     * the output layer, along with the expected value(s) for the input.
     *
     * @param expectedValues the expected values for the test data. Size must match output size.
     */
    public void beginBackPropagation(double[] expectedValues) {
        double[] deltas = calculateOutputDelta(expectedValues);
        NeuronLayer layer = this;
        while (layer.previousLayer != null) {
            // First, do backpropagation. This accesses this layers' weights, so we
            // have to update this layers' weights *after* calculating the deltas for the previous layer.
            double[] newDeltas = layer.previousLayer.calculateBackpropagationDeltas(deltas, layer);
            layer.adjustWeights(deltas);  // when previousLayer is null, we are on the input layer, which has no weights
            deltas = newDeltas;
            layer = layer.previousLayer;
        }
    }

    public void manualExampleBackpropagation(double[] expectedValue) {
        var delta = calculateOutputDelta(expectedValue);
        var outputLayer = this;
        var hiddenLayer = previousLayer;


        var hiddenDeltas = hiddenLayer.calculateBackpropagationDeltas(delta, outputLayer);
        System.out.println("Output Deltas: " + DebugMiniExample.stringify(delta));
        System.out.println("Hidden Deltas: " + DebugMiniExample.stringify(hiddenDeltas));

        hiddenLayer.adjustWeights(hiddenDeltas);
        System.out.println();
    }

    // ----------------------------- DEBUG -----------------------------

    public void debug_setWeights(double[]... valueMatrix) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].debug_setWeights(valueMatrix[i]);
        }
    }
}
