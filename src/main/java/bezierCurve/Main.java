package bezierCurve;

import bezierCurve.curves.CompositeCurve;
import bezierCurve.curves.Curve;
import bezierCurve.curves.SimpleCurve;

import javax.swing.*;
import java.awt.*;

/**
 * Bezier Curve Implementation
 * @author 150001289
 */
public class Main {

    /**
     *
     * @param args Program parameters
     */
    public static void main(String[] args) {
        Main main = new Main();
    }

    private static int DEFAULT_NUM_OF_SAMPLES = 20;

    private JFrame frame;
    private Curve curve;
    private int numOfSamples;

    public Main() {

        this.numOfSamples = DEFAULT_NUM_OF_SAMPLES;

        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(173, 216, 230));

        JToolBar toolBar = createToolbar();
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);

        curve = SimpleCurve.CreateQuadraticCurve(numOfSamples);
        frame.getContentPane().add(curve);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1000, 700);
        frame.setVisible(true);
    }

    /**
     * Creates the toolbar of the application.
     * @return JToolbar component with all buttons and text field
     */
    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        // Point of entry of the number of samples
        JTextField textField = createSamplesTextField();

        // Quadratic Bezier Curve
        JButton quadratic = createQuadraticCurveButton();
        toolBar.add(quadratic);

        //Cubic Bezier Curve
        JButton cubic = createCubicCurveButton();
        toolBar.add(cubic);

        JButton composite = createCompositeCurveButton();
        toolBar.add(composite);

        toolBar.addSeparator();

        JLabel label = new JLabel("Number of samples");
        toolBar.add(label);

        toolBar.add(textField);

        return toolBar;
    }

    /**
     * Creates the text field that allows users to change the number of samples
     * @return The text field
     */
    private JTextField createSamplesTextField() {
        JTextField textField = new JTextField(Integer.toString(DEFAULT_NUM_OF_SAMPLES), 4);
        textField.addActionListener(e -> {
            int samples;
            try {
                samples = Integer.parseInt(textField.getText());
                if (samples < 0) {
                    samples = 20;
                }
            } catch (NumberFormatException exc) {
                samples = 20;
            }
            textField.setText(Integer.toString(samples));
            curve.setNumOfSamples(samples);

            numOfSamples = samples;
        });
        textField.setMaximumSize(textField.getPreferredSize());

        return textField;
    }

    /**
     * Creates the button that allows users to draw a quadratic bezier bezier
     * @return the button
     */
    private JButton createQuadraticCurveButton() {
        JButton quadratic = new JButton("Quadratic");
        quadratic.setToolTipText("Quadratic Bezier Curve");
        quadratic.addActionListener(e -> {
            frame.getContentPane().remove(curve);

            curve = SimpleCurve.CreateQuadraticCurve(numOfSamples);
            frame.getContentPane().add(curve);
            frame.revalidate();
        });

        return quadratic;
    }

    /**
     * Creates the button that allows users to draw a cubic bezier bezier
     * @return the button
     */
    private JButton createCubicCurveButton() {
        JButton cubic = new JButton("Cubic");
        cubic.setToolTipText("Cubic Bezier Curve");
        cubic.addActionListener(e -> {
            frame.getContentPane().remove(curve);

            curve = SimpleCurve.CreateCubicCurve(numOfSamples);
            frame.getContentPane().add(curve);
            frame.revalidate();
        });

        return cubic;
    }

    private JButton createCompositeCurveButton() {
        JButton composite = new JButton("C¹");
        composite.addActionListener(e -> {
            frame.getContentPane().remove(curve);

            curve = new CompositeCurve(numOfSamples);
            frame.getContentPane().add(curve);
            frame.revalidate();
        });
        return composite;
    }


}
