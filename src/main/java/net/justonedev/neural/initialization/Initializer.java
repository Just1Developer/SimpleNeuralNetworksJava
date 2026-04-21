package net.justonedev.neural.initialization;

import net.justonedev.test.ClosedInterval;

import java.util.Random;

public abstract class Initializer {
    private Random random;
    private ClosedInterval range;

    protected void configure(int seed, ClosedInterval initializationInterval) {
        this.range = initializationInterval;
        setRandom(seed);
    }

    protected Random getRandom() {
        return random;
    }

    protected void setRandom(int seed) {
        this.random = new Random(seed);
    }

    protected ClosedInterval getRange() {
        return range;
    }

    public abstract double nextDouble();
}
