package Model.RotationState;

import Model.BoardElement.BoardElement;

import java.awt.*;

public class UpState implements RotationState {
    BoardElement boardElement;

    public UpState(BoardElement boardElement) {
        this.boardElement = boardElement;
    }


    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries >= RotationState.MAX_TRIALS) return;
        if (canUp) {
            boardElement.setYPosition(boardElement.getYPosition() - 1);
        } else {
            boardElement.setRotationState(new RightState(boardElement));
            boardElement.getRotationState().move(false, canRight, canDown, canLeft, numberOfTries + 1);
        }
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.upDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void rotateRight() {
        this.boardElement.setRotationState(new RightState(boardElement));
    }

    @Override
    public void rotateLeft() {
        this.boardElement.setRotationState(new LeftState(boardElement));
    }
}
