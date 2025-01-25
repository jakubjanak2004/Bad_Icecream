package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class DownState extends RotationState {
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
    public void manipulateIce() {
        int playerXFreezingPosition = boardElement.getGameBoard().getPlayer().getXPosition();
        int playerYFreezingPosition = boardElement.getGameBoard().getPlayer().getYPosition();
        boolean freeze  = true;
        playerYFreezingPosition += 1;
        if (boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition)) {
            freeze = false;
        }
        if (playerYFreezingPosition >= boardElement.getGameBoard().getGameBoardLengthY()) {
            return;
        }
        for (int row = playerYFreezingPosition; row < boardElement.getGameBoard().getGameBoardLengthY(); row++) {
            if (!IceManipulator.checkIceLoop(playerXFreezingPosition, row, freeze, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXFreezingPosition, row, freeze, boardElement.getGameBoard());

            sleep();
        }
    }
}
