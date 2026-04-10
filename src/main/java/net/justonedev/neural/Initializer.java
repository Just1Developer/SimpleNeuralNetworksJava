package net.justonedev.neural;

import net.justonedev.test.ClosedInterval;

import java.util.Random;

public class Initializer {
    private static final ClosedInterval DEFAULT_INTERVAL = new ClosedInterval(-0.5, 0.5);

    private final Random random;
    private final ClosedInterval valueInterval;

    public Initializer(int seed) {
        this(seed, DEFAULT_INTERVAL);
    }

    public Initializer(int seed, ClosedInterval valueInterval) {
        this.random = new Random(seed);
        this.valueInterval = valueInterval == null ? DEFAULT_INTERVAL : valueInterval;
    }

    public double nextDouble() {
        return nextDouble(valueInterval);
    }

    public double nextDouble(ClosedInterval range) {
        ClosedInterval interval = range == null ? valueInterval : range;
        return interval.getMinimum() + (random.nextDouble() * (interval.getMaximum() - interval.getMinimum()));
    }
}
