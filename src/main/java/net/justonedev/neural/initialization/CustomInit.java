package net.justonedev.neural.initialization;

import net.justonedev.test.ClosedInterval;

public class CustomInit extends Initializer {
    public CustomInit(int seed, int inputs) {
        double intervalBorder = 1d / (inputs + 1);
        super.configure(seed, new ClosedInterval(-intervalBorder, intervalBorder));
    }

    @Override
    public double nextDouble() {
        return getRange().getMinimum() + (getRandom().nextDouble() * (getRange().getMaximum() - getRange().getMinimum()));
    }
}
