package bezierCurve.points;

import java.awt.*;

public class LinePoint extends MyPoint<Double> {

    private static int DEFAULT_WIDTH = 10;
    private static int DEFAULT_HEIGHT = 10;
    private static Color DEFAULT_COLOR = Color.BLACK;

    private double t;

    public LinePoint(double t, Double x, Double y) {
        this(t, x, y, DEFAULT_COLOR);
    }

    public LinePoint(double t, Double x, Double y, Color color) {
        super(x, y, color, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.t = t;
    }

    public LinePoint perpendicular() {
        return new LinePoint(t, -getY(), getX());
    }

    public double dot(LinePoint point) {
        return getX() * point.getX() + getY() * point.getY();
    }

    public LinePoint unit() {
        double magnitude = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
        return new LinePoint(t, getX() / magnitude, getY() / magnitude);
    }

    public LinePoint vectorToPoint(ControlPoint point) {
        return new LinePoint(t, getX() - point.getX(), getY() - point.getY());
    }

    public double distanceTo(MyPoint point) {
        return Math.sqrt(Math.pow(getX() - point.getX().doubleValue(), 2) + Math.pow(getY() - point.getY().doubleValue(), 2));
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }
}
