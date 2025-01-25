package Model.BoardElement;

import Model.Player.Rotation;
import Model.RotationState.DownState;
import Model.RotationState.LeftState;
import Model.RotationState.RightState;
import Model.RotationState.RotationState;
import Model.RotationState.UpState;

import java.awt.*;

/**
 * Board element is a class representing all elements that can be placed on our gameBoard. Many other objects will be extending it.
 */
public class BoardElement implements on2DBoard, paintable {
    protected int xPosition;
    protected int yPosition;
    protected RotationState rotationState;
    protected Color color;

    public BoardElement(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;

        // setting the state to up when no rotation is set
        setRotationState(new UpState(this));
    }

    public BoardElement(int xPosition, int yPosition, Rotation rot) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;

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
     * Will paint the object at location that it is on, letting the rotation state paint for rotation specific painting
     *
     * @param g             Graphics2D Object for painting
     * @param step          the step of the game Board, it is the with between the lines on the GameBoard
     * @param widthPadding  the Width Padding the GameBoard is moved by from the left side of the window
     * @param heightPadding the Height Padding the GameBoard is moved by from the upper side of the window
     */
    public final void paintTemplate(Graphics2D g, int step, int widthPadding, int heightPadding) {
        paint(g, step, widthPadding, heightPadding);
        rotationState.paint(g, step, widthPadding, heightPadding);
    }

    protected void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
    }

    public void rightDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
    }

    public void leftDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
    }

    public void upDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
    }

    public void downDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
    }

    protected int calcX(int step, int widthPadding) {
        return getXPosition() * step + widthPadding;
    }

    protected int calcY(int step, int heightPadding) {
        return getYPosition() * step + heightPadding;
    }

    public boolean isVisitable() {
        return true;
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

    public void setRot(Rotation rot) {
        setRotationStateFromRotation(rot);
    }
}
