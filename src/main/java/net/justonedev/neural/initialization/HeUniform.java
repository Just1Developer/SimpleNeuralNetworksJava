package net.justonedev.neural.initialization;

import net.justonedev.test.ClosedInterval;

public class HeUniform extends Initializer {
    public HeUniform(int seed, int inputs) {
        double intervalBorder = HeType.HE_UNIFORM.getIntervalBorder(inputs);
        super.configure(seed, new ClosedInterval(-intervalBorder, intervalBorder));
    }

    @Override
    public double nextDouble() {
        return getRange().getMinimum() + (getRandom().nextDouble() * (getRange().getMaximum() - getRange().getMinimum()));
    }
}
