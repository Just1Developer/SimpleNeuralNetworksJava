package net.justonedev.test;

import lombok.Getter;

@Getter
public class ClosedInterval {
    private final double minimum;
    private final double maximum;

    public ClosedInterval(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }
}
