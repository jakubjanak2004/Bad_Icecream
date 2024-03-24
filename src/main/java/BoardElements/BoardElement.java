package BoardElements;

import java.awt.*;

public class BoardElement implements IsOn2DimensionalBoard, isPaintable {
    protected int xPosition;
    protected int yPosition;
    protected int rot;
    protected Color color;

    public Color getColor() {
        return color;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getRot() {
        return rot;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(getColor());
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }

    public BoardElement(int xPosition, int yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public BoardElement(int xPosition, int yPosition, int rot){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rot = rot;
    }
}
