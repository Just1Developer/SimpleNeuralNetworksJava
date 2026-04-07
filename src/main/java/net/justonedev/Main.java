package net.justonedev;

import net.justonedev.neural.NeuralNetwork;
import net.justonedev.neural.activation.LeakyReLU;
import net.justonedev.neural.regularization.NoRegularizer;
import net.justonedev.test.ClosedInterval;
import net.justonedev.test.TestGenerator;
import net.justonedev.ui.Window;

import java.util.List;
import java.util.StringJoiner;

public final class Main {
    private static final int BOARD_WIDTH = 8;
    private static final int HIDDEN_LAYERS = 3;
    private static final int HIDDEN_LAYERS_SIZE = 10;

    private static final int TRAINING_DATA_SIZE = 1000;
    private static final double TEST_DATA_SIZE = 0.1;

    private Main() { }

    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(
                (int) Math.pow(BOARD_WIDTH, 2),
                1,
                HIDDEN_LAYERS,
                HIDDEN_LAYERS_SIZE,
                new LeakyReLU(0.15),
                new NoRegularizer(),
                0);


        TestGenerator generator = new TestGenerator();
        generator.configureGenerator(List.of(new ClosedInterval(0, 0), new ClosedInterval(1, 1)), BOARD_WIDTH, 0);

        //SupervisedTrainer trainer = new SupervisedTrainer(neuralNetwork, new RingDistanceEvaluator(), generator, TRAINING_DATA_SIZE, TEST_DATA_SIZE);


        new Window(neuralNetwork, 30);
    }

    private static void print(double[] array) {
        StringJoiner joiner = new StringJoiner(", ");
        for (double value : array) {
            joiner.add(String.valueOf(value));
        }
        System.out.printf("[%s]%n", joiner);
    }
}