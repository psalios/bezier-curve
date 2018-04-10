package bezierCurve.bezier;

import bezierCurve.points.ControlPoint;
import bezierCurve.points.LinePoint;

import java.util.ArrayList;
import java.util.List;

public class QuadraticBezierCurve extends BezierCurve {

    private static int NUM_OF_CONTROL_POINTS = 3;

    public QuadraticBezierCurve() {
        super(NUM_OF_CONTROL_POINTS);
    }

    /**
     * Solve quadratic equation to find roots between the bezier and the line.
     * @param bezierPoint
     * @return The other root of the quadratic equation.
     */
    public List<Double> calculateRoots(LinePoint bezierPoint, ControlPoint lightSource) {

        List<Double> result = new ArrayList<>();
        if (controlPoints.size() == NUM_OF_CONTROL_POINTS) {
            double A = lightSource.getY() - bezierPoint.getY();
            double B = bezierPoint.getX() - lightSource.getX();
            double C = bezierPoint.getY() * lightSource.getX() - bezierPoint.getX() * lightSource.getY();

            ControlPoint P1 = controlPoints.get(0);
            ControlPoint P2 = controlPoints.get(1);
            ControlPoint P3 = controlPoints.get(2);

            double D = A * (P1.getX() - 2 * P2.getX() + P3.getX());
            double E = A * 2 * (P2.getX() - P1.getX());
            double F = A * P1.getX();

            double G = B * (P1.getY() - 2 * P2.getY() + P3.getY());
            double H = B * 2 * (P2.getY() - P1.getY());
            double I = B * P1.getY();

            double a = D + G;
            double b = E + H;
            double c = C + F + I;

            double root1 = (-b + Math.sqrt(b*b - 4*a*c)) / (2 * a);
            double root2 = (-b - Math.sqrt(b*b - 4*a*c)) / (2 * a);

            // Check if valid numbers
            if (!Double.isNaN(root1) && !Double.isNaN(root2)) {
                result.add(root1);
                result.add(root2);
            }
        }
        return result;
    }

    @Override
    protected LinePoint firstDerivative(double t) {
        ControlPoint p1 = controlPoints.get(0);
        ControlPoint p2 = controlPoints.get(1);
        ControlPoint p3 = controlPoints.get(2);

        double x = firstDerivative(t, p1.getX(), p2.getX(), p3.getX());
        double y = firstDerivative(t, p1.getY(), p2.getY(), p3.getY());

        return new LinePoint(t, x, y);
    }

    private double firstDerivative(double t, double p1, double p2, double p3) {
        return -2 * (1-t) * p1 - 2 * t * p2 + 2 * (1-t) * p2 + 2 * t * p3;
    }
}
