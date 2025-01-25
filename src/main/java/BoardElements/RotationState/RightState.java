package BoardElements.RotationState;

import BoardElements.BoardElement.BoardElement;

import java.awt.*;

public class RightState implements RotationState {
    BoardElement boardElement;

    public RightState(BoardElement boardElement) {
        this.boardElement = boardElement;
    }


    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries >= RotationState.MAX_TRIALS) return;
        if (canRight) {
            boardElement.setXPosition(boardElement.getXPosition() + 1);
        } else {
            boardElement.setRotationState(new DownState(boardElement));
            boardElement.getRotationState().move(canUp, false, canDown, canLeft, numberOfTries + 1);
        }
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.rightDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void rotateRight() {
        this.boardElement.setRotationState(new DownState(boardElement));
    }

    @Override
    public void rotateLeft() {
        this.boardElement.setRotationState(new UpState(boardElement));
    }
}
