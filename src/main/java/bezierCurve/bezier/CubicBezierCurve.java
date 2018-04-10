package bezierCurve.bezier;

import bezierCurve.points.ControlPoint;
import bezierCurve.points.LinePoint;

import java.util.ArrayList;
import java.util.List;

public class CubicBezierCurve extends BezierCurve {

    private static int NUM_OF_CONTROL_POINTS = 4;
    private static final double TWO_PI = 2.0 * Math.PI;
    private static final double FOUR_PI = 4.0 * Math.PI;

    public CubicBezierCurve() {
        super(NUM_OF_CONTROL_POINTS);
    }

    /**
     * Solve cubic equation to find roots between the bezier and the line.
     * @param bezierPoint
     * @return The list of the roots
     */
    public List<Double> calculateRoots(LinePoint bezierPoint, ControlPoint lightSource) {

        List<Double> results = new ArrayList<>();
        if (controlPoints.size() == NUM_OF_CONTROL_POINTS) {
            double A = lightSource.getY() - bezierPoint.getY();
            double B = bezierPoint.getX() - lightSource.getX();
            double C = bezierPoint.getY() * lightSource.getX() - bezierPoint.getX() * lightSource.getY();

            ControlPoint P1 = controlPoints.get(0);
            ControlPoint P2 = controlPoints.get(1);
            ControlPoint P3 = controlPoints.get(2);
            ControlPoint P4 = controlPoints.get(3);

            double D = A * (-P1.getX() + 3*P2.getX() - 3*P3.getX() + P4.getX());
            double E = A * (3*P1.getX() - 6*P2.getX() + 3*P3.getX());
            double F = A * (-3*P1.getX() + 3*P2.getX());
            double G = A * P1.getX();

            double H = B * (-P1.getY() + 3*P2.getY() - 3*P3.getY() + P4.getY());
            double I = B * (3*P1.getY() - 6*P2.getY() + 3*P3.getY());
            double J = B * (-3*P1.getY() + 3*P2.getY());
            double K = B * P1.getY();

            double tmp = D + H;
            double a = (E + I) / tmp;
            double b = (F + J) / tmp;
            double c = (C + G + K) / tmp;

            double Q = (3*b - a*a) / 9.0;
            double R = (9*a*b - 27*c - 2*a*a*a) / 54.0;
            D = Q*Q*Q + R*R;

            if (D > 0.0) {
                // One real root.
                double SQRT_D = Math.sqrt (D);
                double S = Math.cbrt (R + SQRT_D);
                double T = Math.cbrt (R - SQRT_D);

                double root = (S + T) - a / 3.0;
                results.add(root);
            } else if (D < 0.0) {
                double theta = Math.acos (R / Math.sqrt (-Q*Q*Q));
                double SQRT_Q = Math.sqrt (-Q);

                double root = 2.0 * SQRT_Q * Math.cos (theta/3.0) - a / 3.0;
                results.add(root);

                root = 2.0 * SQRT_Q * Math.cos ((theta+TWO_PI)/3.0) - a / 3.0;
                results.add(root);

                root = 2.0 * SQRT_Q * Math.cos ((theta+FOUR_PI)/3.0) - a / 3.0;
                results.add(root);
            } else {
                // Three real roots, at least two equal.
                double CBRT_R = Math.cbrt (R);

                double root = 2*CBRT_R - a / 3.0;
                results.add(root);

                root = CBRT_R - a / 3.0;
                results.add(root);
            }
        }

        return results;
    }

    @Override
    protected LinePoint firstDerivative(double t) {
        ControlPoint p1 = controlPoints.get(0);
        ControlPoint p2 = controlPoints.get(1);
        ControlPoint p3 = controlPoints.get(2);
        ControlPoint p4 = controlPoints.get(3);

        double x = firstDerivative(t, p1.getX(), p2.getX(), p3.getX(), p4.getX());
        double y = firstDerivative(t, p1.getY(), p2.getY(), p3.getY(), p4.getY());

        return new LinePoint(t, x, y);
    }

    private double firstDerivative(double t, double p1, double p2, double p3, double p4) {
        return 3 * ( (p4 - 3*p3 + 3*p2 - p1) * t*t + (2*p3 - 4*p2 + 2*p1) * t + p2 - p1);
    }
}
