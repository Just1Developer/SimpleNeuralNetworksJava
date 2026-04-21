package net.justonedev.neural.initialization;

import net.justonedev.test.ClosedInterval;

public class HeNormal extends Initializer {
    public HeNormal(int seed, int inputs) {
        double intervalBorder = HeType.HE_NORMAL.getIntervalBorder(inputs);
        super.configure(seed, new ClosedInterval(-intervalBorder, intervalBorder));
    }

    @Override
    public double nextDouble() {
        throw new IllegalStateException("He Normal is not implemented yet (requires gaussian distributed sampling)");
    }
}
