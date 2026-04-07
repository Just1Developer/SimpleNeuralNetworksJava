package net.justonedev.ui.drawable;

import net.justonedev.ui.Shape;

public class Circle extends Drawable {
    public Circle(int x, int y) {
        super(x, y);
    }

    @Override
    public Shape getShape() {
        return Shape.CIRCLE;
    }
}
