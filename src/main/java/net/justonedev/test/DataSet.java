package net.justonedev.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    // We use k-means Validation
    @Getter
    private final List<TestDatapoint> trainingData;
    @Getter
    private final List<TestDatapoint> testData;
    @Getter
    private final HardcodedEvaluator evaluator;
    @Getter
    private final int k;
    private final List<List<TestDatapoint>> kValidationSegments;

    public DataSet(TestGenerator generator, HardcodedEvaluator evaluator, int trainingDataSize, double testDataRelativeSize, int k) {
        this(generator, evaluator, trainingDataSize, (int) Math.ceil(trainingDataSize * testDataRelativeSize), k);
    }

    public DataSet(TestGenerator generator, HardcodedEvaluator evaluator, int trainingDataSize, int testDataSize, int k) {
        this.trainingData = generator.generateDatapoints(trainingDataSize, evaluator);
        this.testData = generator.generateDatapoints(testDataSize, evaluator);
        this.evaluator = evaluator;
        this.k = k;
        this.kValidationSegments = new ArrayList<>();
        for (int i = 0; i < trainingDataSize + k; i += k) {
            kValidationSegments.add(trainingData.subList(i, Math.min(i + k, trainingDataSize)));
        }
    }

    public KMeansDataSet getKMeansDataSet(int k) {
        if (k >= kValidationSegments.size()) {
            return null;
        }
        List<TestDatapoint> trainingData = new ArrayList<>();
        List<TestDatapoint> validationData = kValidationSegments.get(k);
        List<TestDatapoint> testData = new ArrayList<>(this.testData);
        for (int i = 0; i < kValidationSegments.size(); i++) {
            if (i == k) continue;
            trainingData.addAll(kValidationSegments.get(i));
        }
        return new KMeansDataSet(trainingData, validationData, testData);
    }
}
