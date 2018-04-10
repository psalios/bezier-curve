package bezierCurve.points;

import bezierCurve.bezier.BezierCurve;
import bezierCurve.utils.Common;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightSource extends ControlPoint {

    private static Color DEFAULT_COLOR = Color.YELLOW;

    public LightSource(Point point) {
        super(point, DEFAULT_COLOR);
    }

    /**
     * Update the colors of a single Bezier Curve
     * @param bezierCurve
     */
    public void updateColors(BezierCurve bezierCurve) {
        updateColors(new ArrayList<BezierCurve>(){{add(bezierCurve);}});
    }

    /**
     * Update the colors of the samples of a list of bezier curves
     * @param bezierCurves
     */
    public void updateColors(List<BezierCurve> bezierCurves) {

        bezierCurves.forEach(bezierCurve -> {
            List<LinePoint> bezierPoints = bezierCurve.getBezierPoints();
            bezierPoints.forEach(bezierPoint -> {

                int i = 0;
                boolean intersect = false;
                while (!intersect && i < bezierCurves.size()) {
                    BezierCurve curve = bezierCurves.get(i);

                    // Get the roots
                    List<Double> roots = curve.calculateRoots(bezierPoint, this);

                    int j = 0;
                    while (!intersect && j < roots.size()) {
                        Double rootT = roots.get(j);

                        // rootT >= 0 and rootT <= 1
                        if ((rootT > 0 || Common.Equal(rootT, 0)) && (rootT < 1 || Common.Equal(rootT, 1))) {
                            if (!curve.equals(bezierCurve) || !Common.Equal(rootT, bezierPoint.getT())) {

                                LinePoint root = curve.calcPoint(rootT);
                                double pointToLightSource = bezierPoint.distanceTo(this);
                                double pointToRootToLightSource = bezierPoint.distanceTo(root) + root.distanceTo(this);

                                // Check if another sample intersects between the current sample and the light source.
                                if (Common.Equal(pointToLightSource, pointToRootToLightSource)) {
                                    bezierPoint.setColor(Color.BLACK);
                                    intersect = true;
                                }
                            }
                        }
                        j++;
                    }

                    i++;
                }

                if (!intersect) {
                    bezierPoint.setColor(bezierCurve.calcColor(bezierPoint, this));
                }
            });
        });
    }
}
