package Model.RotationState;

import Model.BoardElement.BoardElement;

import java.awt.*;

public class DownState implements RotationState {
    BoardElement boardElement;

    public DownState(BoardElement boardElement) {
        this.boardElement = boardElement;
    }


    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries >= RotationState.MAX_TRIALS) return;
        if (canDown) {
            boardElement.setYPosition(boardElement.getYPosition() + 1);
        } else {
            boardElement.setRotationState(new LeftState(boardElement));
            boardElement.getRotationState().move(canUp, canRight, false, canLeft, numberOfTries + 1);
        }
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.downDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void rotateRight() {
        this.boardElement.setRotationState(new LeftState(boardElement));
    }

    @Override
    public void rotateLeft() {
        this.boardElement.setRotationState(new RightState(boardElement));
    }
}
