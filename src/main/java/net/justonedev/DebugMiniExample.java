package net.justonedev;

import net.justonedev.neural.NeuralNetwork;
import net.justonedev.neural.activation.ReLU;
import net.justonedev.neural.regularization.NoRegularizer;

import java.util.StringJoiner;

public class DebugMiniExample {
    // Exam task that we did manually
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(
                2,
                1,
                1,
                2,
                new ReLU(),
                new NoRegularizer(),
                0,
                0.5);

        neuralNetwork.getHiddenLayers().getFirst().debug_setWeights(
                new double[] { 1, 0 },
                new double[] { 0, 1 }
        );

        neuralNetwork.getOutputLayer().debug_setWeights(
                new double[] { 2, -1 }
        );

        double[] x = { 1, -2 };
        double[] y = { 3 };

        var yHat = neuralNetwork.think(x);
        System.out.printf("yHat: %s%n", stringify(yHat));

        neuralNetwork.getOutputLayer().beginBackPropagation(y);
        //neuralNetwork.getOutputLayer().manualExampleBackpropagation(y);

        //new Window(neuralNetwork, 10);
    }

    public static String stringify(double[] array) {
        StringJoiner joiner = new StringJoiner(", ");
        for (double value : array) {
            joiner.add(String.valueOf(value));
        }
        return "[%s]%n".formatted(joiner);
    }
}
