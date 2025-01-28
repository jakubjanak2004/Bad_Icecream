package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class RightState extends RotationState {


    public RightState(BoardElement boardElement) {
        super(boardElement);
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
    public boolean canMove() {
        if (boardElement.getXPosition() >= boardElement.getGameBoard().getGameBoardLengthX() - 1) {
            return false;
        }
        return boardElement.getGameBoard().isVisitable(boardElement.getXPosition() + 1, boardElement.getYPosition());
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        boardElement.rightDirectionPaint(g, step, widthPadding, heightPadding);
    }

    @Override
    public boolean shouldFreeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        return !boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition);
    }

    @Override
    public void freeze(int playerXFreezingPosition, int playerYFreezingPosition) {
        for (int column = playerXFreezingPosition; column < boardElement.getGameBoard().getGameBoardLengthX(); column++) {
            if (!IceManipulator.checkIceLoop(column, playerYFreezingPosition, true, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(column, playerYFreezingPosition, true, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    public void melt(int playerXMeltingPosition, int playerYMeltingPosition) {
        for (int column = playerXMeltingPosition; column < boardElement.getGameBoard().getGameBoardLengthX(); column++) {
            if (!IceManipulator.checkIceLoop(column, playerYMeltingPosition, false, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(column, playerYMeltingPosition, false, boardElement.getGameBoard());

            sleep();
        }
    }

    @Override
    protected int calculateXManipulatingPosition(int x) {
        return x + 1;
    }
}
