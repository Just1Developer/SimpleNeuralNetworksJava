package net.justonedev.neural.trainer;

import java.util.List;

public record TrainIterationErrorResult(List<double[]> trainingErrors, List<double[]> validationErrors, List<double[]> testErrors) {
}
