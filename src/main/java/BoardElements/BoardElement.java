package BoardElements;

import BoardElements.rotationState.DownState;
import BoardElements.rotationState.LeftState;
import BoardElements.rotationState.RightState;
import BoardElements.rotationState.RotationState;
import BoardElements.rotationState.UpState;

import java.awt.*;

/**
 * Board element is a class representing all elements that can be placed on our gameBoard. Many other objects will be extending it.
 */
public class BoardElement implements on2DBoard, paintable {
    protected int xPosition;
    protected int yPosition;
    protected Rotation rot;
    protected RotationState rotationState;
    protected Color color;

    public BoardElement(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public BoardElement(int xPosition, int yPosition, Rotation rot) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rot = rot;

        setRotationStateFromRotation(rot);
    }

    // TODO: remove setting states this way
    private void setRotationStateFromRotation(Rotation rot) {
        switch (rot) {
            case UP -> setRotationState(new UpState(this));
            case RIGHT -> setRotationState(new RightState(this));
            case DOWN -> setRotationState(new DownState(this));
            case LEFT -> setRotationState(new LeftState(this));
        }
    }

    public RotationState getRotationState() {
        return rotationState;
    }

    public void setRotationState(RotationState rotationState) {
        this.rotationState = rotationState;
    }

    /**
     * Will paint the object at location that it is on
     *
     * @param g             Graphics2D Object for painting
     * @param step          the step of the game Board, it is the with between the lines on the GameBoard
     * @param widthPadding  the Width Padding the GameBoard is moved by from the left side of the window
     * @param heightPadding the Height Padding the GameBoard is moved by from the upper side of the window
     */
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {

    }

    @Override
    public String toString() {
        return "<BoardElement: xPosition=" + this.xPosition + " yPosition=" + this.yPosition + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoardElement element) {
            return this.xPosition == element.xPosition && this.yPosition == element.yPosition;
        }
        return false;
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

    public Rotation getRot() {
        return rot;
    }

    public void setRot(Rotation rot) {
        setRotationStateFromRotation(rot);
        this.rot = rot;

    }
}
