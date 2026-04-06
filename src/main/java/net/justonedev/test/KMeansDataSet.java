package net.justonedev.test;

import java.util.List;

public record KMeansDataSet(List<TestDatapoint> trainingData, List<TestDatapoint> validationData, List<TestDatapoint> testData) {
}
