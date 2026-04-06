package net.justonedev;

import net.justonedev.neural.NeuralNetwork;
import net.justonedev.neural.activation.LeakyReLU;
import net.justonedev.neural.regularization.NoRegularizer;

import java.util.StringJoiner;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(3, 2, 2, 3, new LeakyReLU(0.2), new NoRegularizer(), 0);
        var result = neuralNetwork.think(new double[] { 1, 2, 3 });
        print(result);
    }

    private static void print(double[] array) {
        StringJoiner joiner = new StringJoiner(", ");
        for (double value : array) {
            joiner.add(String.valueOf(value));
        }
        System.out.printf("[%s]%n", joiner);
    }
}