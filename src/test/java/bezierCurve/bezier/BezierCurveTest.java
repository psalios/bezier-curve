package bezierCurve.bezier;

import bezierCurve.points.ControlPoint;
import bezierCurve.points.LinePoint;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class BezierCurveTest {

    @Test
    public void test() {

        BezierCurve bezierCurve = new QuadraticBezierCurve();
        assertEquals(3, bezierCurve.getNumOfControlPoints());

        bezierCurve.addControlPoint(new Point(0, 0));
        bezierCurve.addControlPoint(new Point(50, 50));
        assertEquals(2, bezierCurve.getControlPoints().size());

        bezierCurve.getControlPoints().get(0).select(new Point(5, 5));
        assertTrue(bezierCurve.getControlPoints().get(0).isSelected());

        bezierCurve.getControlPoints().get(0).deselect();
        assertFalse(bezierCurve.getControlPoints().get(0).isSelected());

        assertNull(bezierCurve.getControlPoint(new Point(100, 100)));

        assertEquals(bezierCurve.getControlPoints().get(0), bezierCurve.getControlPoint(new Point(10, 10)));

        bezierCurve.addControlPoint(new Point(100, 0));

        List<Double> roots = bezierCurve.calculateRoots(new LinePoint(0d, 0d, 0d), new ControlPoint(10, 10));
        roots.forEach(root -> assertEquals(0, root.doubleValue(), 1e-9));
    }


}