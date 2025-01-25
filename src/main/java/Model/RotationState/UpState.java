package Model.RotationState;

import Model.BoardElement.BoardElement;
import Model.GameBoard.IceManipulator;

import java.awt.*;

public class UpState extends RotationState {
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
    public void manipulateIce() {
        int playerXFreezingPosition = boardElement.getGameBoard().getPlayer().getXPosition();
        int playerYFreezingPosition = boardElement.getGameBoard().getPlayer().getYPosition();
        boolean freeze = true;
        playerYFreezingPosition -= 1;
        if (boardElement.getGameBoard().isFrozenAtLoc(playerXFreezingPosition, playerYFreezingPosition)) {
            freeze = false;
        }
        if (playerYFreezingPosition < 0) {
            return;
        }
        for (int row = playerYFreezingPosition; row >= 0; row--) {
            if (!IceManipulator.checkIceLoop(playerXFreezingPosition, row, freeze, boardElement.getGameBoard())) {
                return;
            }

            IceManipulator.changeArray(playerXFreezingPosition, row, freeze, boardElement.getGameBoard());

            sleep();
        }
    }
}
