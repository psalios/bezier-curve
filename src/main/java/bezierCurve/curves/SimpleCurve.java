package bezierCurve.curves;

import bezierCurve.bezier.BezierCurve;
import bezierCurve.bezier.CubicBezierCurve;
import bezierCurve.bezier.QuadraticBezierCurve;
import bezierCurve.points.ControlPoint;
import bezierCurve.points.LightSource;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SimpleCurve extends Curve {

    protected BezierCurve bezierCurve;

    private SimpleCurve(int numOfSamples) {
        super(numOfSamples);
    }

    @Override
    protected void update() {
        bezierCurve.updatePoints(getNumOfSamples());
        if (lightSource != null) {
            lightSource.updateColors(bezierCurve);
        }
        repaint();
    }

    /**
     * Create and return a quadratic curve
     * @param numOfSamples The number of samples
     * @return The Bezier curve
     */
    public static SimpleCurve CreateQuadraticCurve(int numOfSamples) {
        SimpleCurve curve = new SimpleCurve(numOfSamples);
        curve.bezierCurve = new QuadraticBezierCurve();

        return curve;
    }

    /**
     * Create and return a cubic curve
     * @param numOfSamples The number of samples
     * @return The Bezier curve
     */
    public static SimpleCurve CreateCubicCurve(int numOfSamples) {
        SimpleCurve curve = new SimpleCurve(numOfSamples);
        curve.bezierCurve = new CubicBezierCurve();

        return curve;
    }

    @Override
    public void paint(Graphics g) {

        // Draw the control points to the panel
        bezierCurve.getControlPoints().forEach(controlPoint -> controlPoint.draw(g));

        // Draw the samples
        bezierCurve.drawLine(g);

        // Draw the light source
        if (lightSource != null) {
            lightSource.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();

        ControlPoint controlPoint = bezierCurve.getControlPoint(point);
        if (controlPoint == null && lightSource != null && lightSource.overlap(point)) {
            controlPoint = lightSource;
        }

        if (controlPoint == null) {
            if (bezierCurve.getControlPoints().size() < bezierCurve.getNumOfControlPoints()) {
                bezierCurve.addControlPoint(point);
                update();
            } else {
                addLightSource(point);
            }
        } else {
            controlPoint.select(point);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        bezierCurve.deselect();
        if (lightSource != null) {
            lightSource.deselect();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        bezierCurve.dragPoint(e.getPoint());
        if (lightSource != null && lightSource.isSelected()) {
            lightSource.drag(e.getPoint());
        }

        update();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();

        ControlPoint controlPoint = bezierCurve.getControlPoint(point);
        if (controlPoint == null && lightSource != null && lightSource.overlap(point)) {
            controlPoint = lightSource;
        }

        // Over control point - change cursor to hand
        if (controlPoint != null) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Adds light source to a given point.
     * @param p
     */
    private void addLightSource(Point p) {
        if (lightSource == null) {
            lightSource = new LightSource(p);

            lightSource.updateColors(bezierCurve);
            repaint();
        }
    }
}
