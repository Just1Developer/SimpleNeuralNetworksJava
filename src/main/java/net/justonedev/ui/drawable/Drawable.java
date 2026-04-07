package net.justonedev.ui.drawable;

import lombok.Getter;
import net.justonedev.ui.Canvas;
import net.justonedev.ui.Shape;

@Getter
public abstract class Drawable {

    private final int x;
    private final int y;

    protected Drawable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getCanvasX(Canvas canvas) {
        return translateCoordinate(x, canvas.getCameraOffsetX(), canvas);
    }

    public int getCanvasY(Canvas canvas) {
        return translateCoordinate(y, canvas.getCameraOffsetY(), canvas);
    }

    protected int translateCoordinate(int coordinate, int offset, Canvas canvas) {
        return (int) Math.round((coordinate + offset) * canvas.getZoomLevel());
    }

    public abstract Shape getShape();

}
