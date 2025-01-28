package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class UpState extends RotationState {


    public UpState(BoardElement boardElement) {
        super(boardElement);
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
    public boolean canMove() {
        if (boardElement.getYPosition() <= 0) {
            return false;
        }
        return boardElement.getGameBoard().isVisitable(boardElement.getXPosition(), boardElement.getYPosition() - 1);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.upDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public boolean shouldFreeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        return !boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition);
    }

    @Override
    protected void freeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        for (int row = playerYFreezingPosition; row >= 0; row--) {
            if (!IceManipulator.checkIceLoop(playerXFreezingPosition, row, true, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXFreezingPosition, row, true, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    protected void melt(int playerXMeltingPosition, int playerYMeltingPosition) {
        for (int row = playerYMeltingPosition; row >= 0; row--) {
            if (!IceManipulator.checkIceLoop(playerXMeltingPosition, row, false, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXMeltingPosition, row, false, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    protected int calculateYManipulatingPosition(int y) {
        return y - 1;
    }
}
