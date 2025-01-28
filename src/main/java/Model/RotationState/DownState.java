package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class DownState extends RotationState {

    public DownState(BoardElement boardElement) {
        super(boardElement);
    }

    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries >= RotationState.MAX_TRIALS) return;
        if (canMove()) {
            boardElement.setYPosition(boardElement.getYPosition() + 1);
        } else {
            boardElement.setRotationState(new LeftState(boardElement));
            boardElement.getRotationState().move(canUp, canRight, false, canLeft, numberOfTries + 1);
        }
    }

    @Override
    public boolean canMove() {
        if (boardElement.getYPosition() >= boardElement.getGameBoard().getGameBoardLengthY()) {
            return false;
        }
        return boardElement.getGameBoard().isVisitable(boardElement.getXPosition(), boardElement.getYPosition() + 1);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.downDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public boolean shouldFreeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        return !boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition);
    }

    @Override
    public void freeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        for (int row = playerYFreezingPosition; row < boardElement.getGameBoard().getGameBoardLengthY(); row++) {
            if (!IceManipulator.checkIceLoop(playerXFreezingPosition, row, true, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXFreezingPosition, row, true, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    public void melt(int playerXMeltingPosition, int playerYMeltingPosition) {
        for (int row = playerYMeltingPosition; row < boardElement.getGameBoard().getGameBoardLengthY(); row++) {
            if (!IceManipulator.checkIceLoop(playerXMeltingPosition, row, false, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXMeltingPosition, row, false, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    protected int calculateYManipulatingPosition(int y) {
        return y + 1;
    }
}
