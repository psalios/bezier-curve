package bezierCurve.bezier;

import bezierCurve.utils.Common;
import org.apache.commons.math3.util.CombinatoricsUtils;
import bezierCurve.points.ControlPoint;
import bezierCurve.points.LinePoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract Bezier Curve class that implements common functionality on bezier curves.
 * @author 150001289
 */
public abstract class BezierCurve {

    int numOfControlPoints;
    List<ControlPoint> controlPoints;

    List<LinePoint> bezierPoints;

    BezierCurve(int numOfControlPoints) {
        this.numOfControlPoints = numOfControlPoints;
        this.controlPoints = new ArrayList<>();

        this.bezierPoints = new ArrayList<>();
    }

    /**
     * Draws all the samples on the panel
     * @param g The graphics where we draw the components
     */
    public void drawLine(Graphics g) {
        bezierPoints.forEach(bezierPoint -> {
            g.setColor(bezierPoint.getColor());
            g.fillOval(bezierPoint.startX(), bezierPoint.startY(), bezierPoint.getWidth(), bezierPoint.getHeight());
        });
    }

    /**
     * Searches and finds the control point that overlaps with the point on JPanel
     * @param p The point we are looking for
     * @return The control point associated
     */
    public ControlPoint getControlPoint(Point p) {
        return controlPoints.stream()
                .filter(controlPoint -> controlPoint.overlap(p))
                .findAny()
                .orElse(null);
    }

    /**
     * Finds which control points are selected when dragging.
     * @return The control points that are selected
     */
    public List<ControlPoint> getSelectedControlPoints() {
        return controlPoints.stream()
                .filter(ControlPoint::isSelected)
                .collect(Collectors.toList());
    }

    /**
     * Adds a control point to a given point.
     * @param p The point
     */
    public void addControlPoint(Point p) {
        if (controlPoints.size() < numOfControlPoints) {
            controlPoints.add(new ControlPoint(p));
        }
    }

    public void addControlPoint(ControlPoint point) {
        if (controlPoints.size() < numOfControlPoints) {
            controlPoints.add(new ControlPoint(point));
        }
    }

    public int getNumOfControlPoints() {
        return numOfControlPoints;
    }

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    /**
     * Updates the positions of the sample points
     */
     public void updatePoints(int numOfSamples) {

        if (controlPoints.size() == numOfControlPoints) {
            double increment = 1d / (numOfSamples - 1);

            bezierPoints.clear();
            for(double t = 0; (t<1d) || Common.Equal(t, 1); t+=increment) {
                bezierPoints.add(calcPoint(t));
            }
        }
    }

    /**
     * Deselects all selected control points
     */
    public void deselect() {
        getSelectedControlPoints().forEach(ControlPoint::deselect);
    }

    /**
     * Drags all selected points given the point of reference.
     * @param point Point where the user clicked
     */
    public void dragPoint(Point point) {
        getSelectedControlPoints().forEach(controlPoint -> controlPoint.drag(point));
    }

    /**
     * Given a parameter t - it calculates the sample point of the bezier bezier
     * @param t
     * @return the sample point of the bezier bezier
     */
    public LinePoint calcPoint(double t) {
        double x = 0, y = 0;
        for(int i=0;i<controlPoints.size();i++) {
            double tmp = CombinatoricsUtils.binomialCoefficient(numOfControlPoints-1, i) * Math.pow(1 - t, numOfControlPoints - i -1) * Math.pow(t, i);

            x += tmp * controlPoints.get(i).getX();
            y += tmp * controlPoints.get(i).getY();
        }

        return new LinePoint(t, x, y);
    }

    /**
     * It calculates the illumination of a given point
     * @param bezierPoint
     * @return
     */
    public Color calcColor(LinePoint bezierPoint, ControlPoint lightSource) {
        LinePoint normal = firstDerivative(bezierPoint.getT()).unit().perpendicular();
        LinePoint toLightSource = bezierPoint.vectorToPoint(lightSource).unit();

        float dot = Math.max((float) normal.dot(toLightSource), 0f);
        return new Color(dot, dot, dot);
    }

    public List<LinePoint> getBezierPoints() {
        return bezierPoints;
    }

    public void setBezierPoints(List<LinePoint> bezierPoints) {
        this.bezierPoints = bezierPoints;
    }

    /**
     * Finds the roots between the curve and the straight line between bezierPoint and lightSource
     * @param bezierPoint point of the straight line
     * @param lightSource point of the straight line
     * @return the list of roots
     */
    public abstract List<Double> calculateRoots(LinePoint bezierPoint, ControlPoint lightSource);

    /**
     * Calculates the first derivative of the curve on the point
     * @param t
     * @return the derivative on both x and y axis
     */
    protected abstract LinePoint firstDerivative(double t);
}
