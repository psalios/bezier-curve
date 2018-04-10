package bezierCurve.points;

import java.awt.*;

public class ControlPoint extends MyPoint<Integer> {

    private static int DEFAULT_WIDTH = 20;
    private static int DEFAULT_HEIGHT = 20;
    private static Color DEFAULT_COLOR = Color.BLACK;

    private Point selectPoint;
    private int dragging;

    public ControlPoint(Point point) {
        this(point, DEFAULT_COLOR);
    }

    public ControlPoint(ControlPoint point) {
        this(point, DEFAULT_COLOR);
    }

    public ControlPoint(ControlPoint point, Color color) {
        this(point.getX(), point.getY(), color);
    }

    public ControlPoint(Point point, Color color) {
        this((int) point.getX(), (int)point.getY(), color);
    }

    public ControlPoint(int x, int y) {
        this(x, y, DEFAULT_COLOR);
    }

    public ControlPoint(int x, int y, Color color) {
        super(x, y, color, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Check if the circle is on the given point
     * @param p
     * @return True/false if the circle overlaps with the given point
     */
    public boolean overlap(Point p) {
        return startX() <= p.getX() && stopX() >= p.getX() && startY() <= p.getY() && stopY() >= p.getY();
    }

    /**
     * Calculates the vector from this point to a given control point
     * @param point
     * @return
     */
    public ControlPoint vectorToPoint(ControlPoint point) {
        return new ControlPoint(getX() - point.getX(), getY() - point.getY());
    }

    /**
     * Adds a vector to a given control point
     * @param point
     * @return
     */
    public ControlPoint addVector(ControlPoint point) {
        return new ControlPoint(getX() + point.getX(), getY() + point.getY());
    }

    /**
     * Select the control point
     * @param point
     */
    public void select(Point point) {
        this.selectPoint = point;
        dragging = 1;
    }

    /**
     * Select the control point for inverse direction
     * @param point
     */
    public void inverseSelect(Point point) {
        this.selectPoint = point;
        dragging = -1;
    }

    /**
     * Deselect the control point
     */
    public void deselect() {
        dragging = 0;
    }

    /**
     *
     * @return True/false if the control point is selected or not
     */
    public boolean isSelected() {
        return dragging != 0;
    }

    /**
     * Drags the control point to the given coordinates
     * @param point
     */
    public void drag(Point point) {
        if (isSelected()) {
            this.setX((int) (this.getX() + dragging * (point.getX() - selectPoint.getX())));
            this.setY((int) (this.getY() + dragging * (point.getY() - selectPoint.getY())));
            selectPoint = point;
        }
    }
}
