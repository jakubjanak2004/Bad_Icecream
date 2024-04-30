package BoardElements;

import java.awt.*;

/**
 * Board element is a class representing all elements that can be placed on our gameBoard. Many other objects will be extending it.
 */
public class BoardElement implements IsOn2DimensionalBoard, isPaintable {
    protected int xPosition;
    protected int yPosition;
    protected int rot;
    protected Color color;

    public BoardElement(int xPosition, int yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public BoardElement(int xPosition, int yPosition, int rot){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rot = rot;
    }

    /**
     * Will paint the object at location that it is on
     * @param g Graphics2D Object for painting
     * @param step the step of the game Board, it is the with between the lines on the GameBoard
     * @param widthPadding the Width Padding the GameBoard is moved by from the left side of the window
     * @param heightPadding the Height Padding the GameBoard is moved by from the upper side of the window
     */
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {

    }

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
}
