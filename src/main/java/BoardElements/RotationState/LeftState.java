package BoardElements.RotationState;

import BoardElements.BoardElement.BoardElement;

import java.awt.*;

public class LeftState implements RotationState {
    BoardElement boardElement;

    public LeftState(BoardElement boardElement) {
        this.boardElement = boardElement;
    }

    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries >= RotationState.MAX_TRIALS) return;
        if (canLeft) {
            boardElement.setXPosition(boardElement.getXPosition() - 1);
        } else {
            boardElement.setRotationState(new UpState(boardElement));
            boardElement.getRotationState().move(canUp, canRight, canDown, false, numberOfTries + 1);
        }
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.leftDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void rotateRight() {
        this.boardElement.setRotationState(new UpState(boardElement));
    }

    @Override
    public void rotateLeft() {
        this.boardElement.setRotationState(new DownState(boardElement));
    }
}
