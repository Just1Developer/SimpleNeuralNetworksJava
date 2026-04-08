package net.justonedev.neural.trainer;

import lombok.Getter;
import net.justonedev.neural.NeuralNetwork;
import net.justonedev.test.DataSet;
import net.justonedev.test.HardcodedEvaluator;
import net.justonedev.test.KMeansDataSet;
import net.justonedev.test.TestDatapoint;
import net.justonedev.test.TestGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple supervised trainer that uses stochastic gradient descent (immediate weight updates).
 */
public class SupervisedTrainer {
    private final NeuralNetwork neuralNetwork;
    private final DataSet dataSet;

    public SupervisedTrainer(NeuralNetwork learner, HardcodedEvaluator evaluator, TestGenerator generator, int trainingDataSize, double testDataRelativeSize) {
        this.neuralNetwork = learner;
        this.dataSet = new DataSet(generator, evaluator, trainingDataSize, testDataRelativeSize, (trainingDataSize + 9) / 10);
    }

    public void train(int iterations) {
        AverageError beginError = new AverageError(stochasticGradientDescent());
        AverageError endError = null;
        for (int i = 1; i < iterations; i++) {
            System.out.println("Begin Iteration " + i);
            endError = new AverageError(stochasticGradientDescent());
            System.out.println("Average Error: " + endError);
        }
        System.out.println("Begin Error: " + beginError);
        System.out.println("End Error: " + endError);
    }

    public List<TrainIterationErrorResult> stochasticGradientDescent() {
        List<TrainIterationErrorResult> results = new ArrayList<>();
        for (int k = 0; k < dataSet.getK(); k++) {
            var kMeansDataSet = dataSet.getKMeansDataSet(k);
            if (kMeansDataSet == null) continue;
            results.add(singleCrossValidation(kMeansDataSet));
        }
        return results;
    }

    private TrainIterationErrorResult singleCrossValidation(KMeansDataSet dataSet) {
        List<double[]> trainingErrors = new ArrayList<>();
        List<double[]> validationErrors = new ArrayList<>();
        List<double[]> testErrors = new ArrayList<>();
        trainOnDatapoints(dataSet.trainingData(), trainingErrors, true);
        trainOnDatapoints(dataSet.validationData(), validationErrors, false);
        trainOnDatapoints(dataSet.testData(), testErrors, false);
        return new TrainIterationErrorResult(trainingErrors, validationErrors, testErrors);
    }

    private void trainOnDatapoints(Collection<TestDatapoint> datapoints, Collection<double[]> errors, boolean train) {
        for (TestDatapoint datapoint : datapoints) {
            errors.add(getError(datapoint, train));
        }
    }

    private double[] getError(TestDatapoint datapoint, boolean train) {
        // Debug: Check Datapoint
        for (var x : datapoint.board().getRaw()) {
            if (Double.isNaN(x)) {
                System.out.println("Datapoint contains NaN");
            }
        }
        for (var x : datapoint.target()) {
            if (Double.isNaN(x)) {
                System.out.println("Datapoint target contains NaN");
            }
        }


        double[] result = this.neuralNetwork.think(datapoint.board().getRaw());
        if (train) {
            neuralNetwork.getOutputLayer().beginBackPropagation(datapoint.target());
        }
        if (Double.isNaN(result[0])) {
            //System.out.println("Is NaN");
            //System.exit(1);
        }
        double[] error = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            error[i] = datapoint.target()[i] - result[i];
        }
        return error;
    }

    @Getter
    private static class AverageError {
        private final double trainingError;
        private final double validationError;
        private final double testError;

        private AverageError(List<TrainIterationErrorResult> errorResults) {
            double trainingError = 0;
            double validationError = 0;
            double testError = 0;
            for (TrainIterationErrorResult errorResult : errorResults) {
                trainingError += average(errorResult.trainingErrors());
                validationError += average(errorResult.trainingErrors());
                testError += average(errorResult.trainingErrors());
            }
            this.trainingError = trainingError;
            this.validationError = validationError;
            this.testError = testError;
        }

        private static double average(List<double[]> values) {
            double sum = 0;
            int counted = 0;
            for (double[] value : values) {
                double average = average(value);
                if (Double.isNaN(average)) continue;
                sum += average;
                counted++;
            }
            if (counted == 0) return 0;
            return sum / counted;
        }

        private static double average(double[] values) {
            double sum = 0;
            int counted = 0;
            for (double value : values) {
                if (Double.isNaN(value)) continue;
                sum += value;
                counted++;
            }
            return sum / counted;
        }

        @Override
        public String toString() {
            return "[trainingError: %f, validationError: %f, testError: %f]".formatted(trainingError, validationError, testError);
        }
    }
}
