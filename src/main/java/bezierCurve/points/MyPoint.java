package bezierCurve.points;

import java.awt.*;

public abstract class MyPoint<T extends Number> {

    private T x;
    private T y;
    private int width;
    private int height;
    private Color color;

    public MyPoint(T x, T y, Color color, int width,int height) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(startX(), startY(), width, height);
    }

    public int startX() {
        return x.intValue() - width / 2;
    }
    public int stopX() {
        return x.intValue() + width / 2;
    }
    public int startY() {
        return y.intValue() - height / 2;
    }
    public int stopY() {
        return y.intValue() + height / 2;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
