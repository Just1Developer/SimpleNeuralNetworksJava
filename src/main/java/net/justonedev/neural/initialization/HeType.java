package net.justonedev.neural.initialization;

public enum HeType {
    HE_NORMAL(
            inputs -> 0.5 * Math.sqrt(2d / inputs)  // gaussian distribution with deviation of Math.sqrt(2d / inputs)
    ),
    HE_UNIFORM(
            inputs -> Math.sqrt(6d / inputs)
    );

    private final BorderGenerator generator;

    HeType(BorderGenerator generator) {
        this.generator = generator;
    }

    public double getIntervalBorder(int inputs) {
        return generator.generate(inputs);
    }

    private interface BorderGenerator {
        double generate(int inputs);
    }
}
