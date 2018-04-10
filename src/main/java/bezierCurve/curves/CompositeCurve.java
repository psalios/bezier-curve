package bezierCurve.curves;

import bezierCurve.bezier.BezierCurve;
import bezierCurve.bezier.CubicBezierCurve;
import bezierCurve.points.ControlPoint;
import bezierCurve.points.LightSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositeCurve extends Curve {

    private static int FIRST  = 0;
    private static int SECOND = 1;
    private static int THIRD  = 2;
    private static int LAST   = 3;

    private List<BezierCurve> bezierCurves;
    private List<Line2D> lines;

    public CompositeCurve(int numOfSamples) {
        super(numOfSamples);

        bezierCurves = new ArrayList<>();
        bezierCurves.add(new CubicBezierCurve());

        lines = new ArrayList<>();
    }

    @Override
    protected void update() {
        bezierCurves.forEach(bezierCurve -> {
            bezierCurve.updatePoints(getNumOfSamples());
        });
        if (lightSource != null) {
            lightSource.updateColors(bezierCurves);
        }
        updateLines();
        repaint();
    }

    @Override
    public void paint(Graphics g) {

        bezierCurves.forEach(bezierCurve -> {
            // Draw the control points to the panel
            bezierCurve.getControlPoints().forEach(controlPoint -> controlPoint.draw(g));

            // Draw the samples
            bezierCurve.drawLine(g);
        });

        // Draw the red lines
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        lines.forEach(g2::draw);

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

        // Find if there is a control point at this position
        ControlPoint controlPoint = bezierCurves.stream()
                .map(bezierCurve -> bezierCurve.getControlPoint(point))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(lightSource != null && lightSource.overlap(point) ? lightSource : null);

        if (controlPoint == null) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                // Left mouse - add control point
                addControlPoint(point);
                this.repaint();
            } else if (SwingUtilities.isRightMouseButton(e)) {
                // Right mouse - add light source
                addLightSource(point);
            }
        } else {
            // Select all control points that are on this position
            selectControlPoints(point);
            if (lightSource != null && lightSource.overlap(point)) {
                lightSource.select(point);
            }
        }
    }

    /**
     * Add a control point at the given coordinates
     * @param point The coordinates where the user clicked
     */
    private void addControlPoint(Point point) {
        BezierCurve last = bezierCurves.get(bezierCurves.size() - 1);
        last.addControlPoint(point);
        last.updatePoints(getNumOfSamples());

        if (last.getControlPoints().size() == last.getNumOfControlPoints()) {
            CubicBezierCurve newCurve = new CubicBezierCurve();

            List<ControlPoint> controlPoints = last.getControlPoints();

            ControlPoint first = controlPoints.get(LAST);
            newCurve.addControlPoint(first);

            ControlPoint previous = controlPoints.get(THIRD);
            ControlPoint second = first.addVector(first.vectorToPoint(previous));
            newCurve.addControlPoint(second);

            lines.add(new Line2D.Float(previous.getX(), previous.getY(), second.getX(), second.getY()));

            bezierCurves.add(newCurve);
        }
    }

    /**
     * Select all control points that overlap on the given coordinates
     * @param point The coordinates where the user clicked
     */
    private void selectControlPoints(Point point) {
        for(int i=0;i<bezierCurves.size();i++) {
            BezierCurve previous = i > 0 ? bezierCurves.get(i-1) : null;
            BezierCurve curve = bezierCurves.get(i);
            BezierCurve next = i + 1 < bezierCurves.size() ? bezierCurves.get(i+1) : null;

            ControlPoint cp = curve.getControlPoint(point);
            if (cp != null) {
                cp.select(point);

                if (next != null && cp.equals(curve.getControlPoints().get(THIRD))) {
                    next.getControlPoints().get(SECOND).inverseSelect(point);
                }
                if (previous != null && cp.equals(curve.getControlPoints().get(SECOND))) {
                    previous.getControlPoints().get(THIRD).inverseSelect(point);
                }

                if (cp.equals(curve.getControlPoints().get(FIRST))) {
                    curve.getControlPoints().get(SECOND).select(point);
                }
                if (curve.getControlPoints().size() > LAST && cp.equals(curve.getControlPoints().get(LAST))) {
                    curve.getControlPoints().get(THIRD).select(point);
                }
            }
        }
    }

    /**
     * Create light source at given coordinates
     * @param point The coordinates where the user clicked
     */
    private void addLightSource(Point point) {
        if (lightSource == null) {
            lightSource = new LightSource(point);
            lightSource.updateColors(bezierCurves);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        bezierCurves.forEach(BezierCurve::deselect);
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
        Point point = e.getPoint();

        bezierCurves.stream()
                .filter(bezierCurve -> bezierCurve.getSelectedControlPoints().size() > 0)
                .forEach(bezierCurve -> {
                    bezierCurve.getSelectedControlPoints().forEach(controlPoint -> controlPoint.drag(point));
                });

        update();

        if (lightSource != null && lightSource.isSelected()) {
            lightSource.drag(point);
            lightSource.updateColors(bezierCurves);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();

        ControlPoint controlPoint = bezierCurves.stream()
                .filter(bezierCurve -> bezierCurve.getControlPoint(point) != null)
                .map(bezierCurve -> bezierCurve.getControlPoint(point))
                .findAny()
                .orElse(lightSource != null && lightSource.overlap(point) ? lightSource : null);

        if (controlPoint != null) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Updates the coordinates of the lines
     */
    private void updateLines() {
        lines.clear();
        for(int i=0;i<bezierCurves.size() - 1;i++) {
            ControlPoint from = bezierCurves.get(i).getControlPoints().get(bezierCurves.get(i).getControlPoints().size() - 2);
            ControlPoint to = bezierCurves.get(i + 1).getControlPoints().get(1);
            lines.add(new Line2D.Float(from.getX(), from.getY(), to.getX(), to.getY()));
        }
    }
}
