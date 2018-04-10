package bezierCurve.curves;

import bezierCurve.points.LightSource;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Implements the JComponent of the curve. It also listens to mouse and mouse motion
 * @author 150001289
 */
public abstract class Curve extends JComponent implements MouseListener, MouseMotionListener {

    private int numOfSamples;
    protected LightSource lightSource;

    public Curve(int numOfSamples) {
        this.numOfSamples = numOfSamples;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int getNumOfSamples() {
        return numOfSamples;
    }

    public void setNumOfSamples(int numOfSamples) {
        this.numOfSamples = numOfSamples;
        update();

        repaint();
    }

    /**
     * Updates the samples (both position and lightning)
     */
    protected abstract void update();
}
