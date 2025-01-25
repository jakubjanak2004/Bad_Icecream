package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class RightState extends RotationState {
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
    public void manipulateIce() {
        int playerXFreezingPosition = boardElement.getGameBoard().getPlayer().getXPosition();
        int playerYFreezingPosition = boardElement.getGameBoard().getPlayer().getYPosition();
        boolean freeze = true;
        playerXFreezingPosition += 1;
        if (boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition)) {
            freeze = false;
        }
        if (playerXFreezingPosition >= boardElement.getGameBoard().getGameBoardLengthX()) {
            return;
        }
        for (int column = playerXFreezingPosition; column < boardElement.getGameBoard().getGameBoardLengthX(); column++) {
            if (!IceManipulator.checkIceLoop(column, playerYFreezingPosition, freeze, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(column, playerYFreezingPosition, freeze, boardElement.getGameBoard());

            sleep();
        }
    }
}
