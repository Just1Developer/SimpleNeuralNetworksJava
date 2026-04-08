package net.justonedev;

import net.justonedev.neural.NeuralNetwork;
import net.justonedev.neural.activation.LeakyReLU;
import net.justonedev.neural.regularization.WeightDecayRegularizer;
import net.justonedev.neural.trainer.SupervisedTrainer;
import net.justonedev.test.ClosedInterval;
import net.justonedev.test.RingDistanceEvaluator;
import net.justonedev.test.TestGenerator;

import java.util.List;

public final class Main {
    private static final int BOARD_WIDTH = 8;
    private static final int HIDDEN_LAYERS = 3;
    private static final int HIDDEN_LAYERS_SIZE = 10;

    private static final int TRAINING_DATA_SIZE = 1000;
    private static final double TEST_DATA_SIZE = 0.1;

    private static final double LEARNING_RATE = 0.3;

    private Main() { }

    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(
                (int) Math.pow(BOARD_WIDTH, 2),
                1,
                HIDDEN_LAYERS,
                HIDDEN_LAYERS_SIZE,
                new LeakyReLU(0.15),
                new WeightDecayRegularizer(0.1),
                0,
                LEARNING_RATE);

        //new Window(neuralNetwork, 30);

        TestGenerator generator = new TestGenerator();
        generator.configureGenerator(List.of(new ClosedInterval(0, 0), new ClosedInterval(1, 1)), BOARD_WIDTH, 0);

        SupervisedTrainer trainer = new SupervisedTrainer(neuralNetwork, new RingDistanceEvaluator(), generator, TRAINING_DATA_SIZE, TEST_DATA_SIZE);
        trainer.train(2);
    }
}