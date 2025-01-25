package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class LeftState extends RotationState {
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
    public void manipulateIce() {
        int playerXFreezingPosition = boardElement.getGameBoard().getPlayer().getXPosition();
        int playerYFreezingPosition = boardElement.getGameBoard().getPlayer().getYPosition();
        boolean freeze = true;
        playerXFreezingPosition -= 1;
        if (boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition)) {
            freeze = false;
        }
        if (playerXFreezingPosition < 0) {
            return;
        }
        for (int column = playerXFreezingPosition; column >= 0; column--) {
            if (!IceManipulator.checkIceLoop(column, playerYFreezingPosition, freeze, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(column, playerYFreezingPosition, freeze, boardElement.getGameBoard());

            sleep();
        }
    }
}
