package net.justonedev.ui.drawable;

import net.justonedev.ui.Canvas;
import net.justonedev.ui.Shape;

public class Line extends Drawable {
    private final int toX;
    private final int toY;

    public Line(Circle fromNeuron, Circle toNeuron) {
        super(fromNeuron.getX(), fromNeuron.getY());
        this.toX = toNeuron.getX();
        this.toY = toNeuron.getY();
    }

    public int getCanvasSourceX(Canvas canvas) {
        return super.getCanvasX(canvas);
    }

    public int getCanvasSourceY(Canvas canvas) {
        return super.getCanvasY(canvas);
    }

    public int getCanvasTargetX(Canvas canvas) {
        return super.translateCoordinate(this.toX, canvas.getCameraOffsetX(), canvas);
    }

    public int getCanvasTargetY(Canvas canvas) {
        return super.translateCoordinate(this.toY, canvas.getCameraOffsetY(), canvas);
    }

    @Override
    public Shape getShape() {
        return Shape.LINE;
    }
}
