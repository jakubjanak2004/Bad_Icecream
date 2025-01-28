package Model.RotationState;

import Model.BoardElement.BoardElement;

import java.awt.*;

public abstract class RotationState {
    protected static final int MAX_TRIALS = 3;

    protected final BoardElement boardElement;

    public RotationState(BoardElement boardElement) {
        this.boardElement = boardElement;
    }

    protected static void sleep() {
        try {
            Thread.sleep(Model.GameBoard.IceManipulator.MILLIS_WAIT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public final void manipulateIceTemplate() {
        int playerXManipulationPosition = calculateXManipulatingPosition(boardElement.getGameBoard().getPlayer().getXPosition());
        int playerYManipulationPosition = calculateYManipulatingPosition(boardElement.getGameBoard().getPlayer().getYPosition());
        if (boardElement.getGameBoard().isOutside(playerXManipulationPosition, playerYManipulationPosition)) {
            return;
        }
        if (shouldFreeze(playerXManipulationPosition, playerYManipulationPosition)) {
            freeze(playerXManipulationPosition, playerYManipulationPosition);
        } else {
            melt(playerXManipulationPosition, playerYManipulationPosition);
        }
    }

    abstract public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries);

    abstract public boolean canMove();

    abstract public void paint(Graphics2D g, int step, int widthPadding, int heightPadding);

    abstract protected boolean shouldFreeze(int playerXFreezingPosition, int playerYFreezingPosition);

    abstract protected void freeze(int playerXFreezingPosition, int playerYFreezingPosition);

    abstract protected void melt(int playerXFreezingPosition, int playerYFreezingPosition);

    protected int calculateXManipulatingPosition(int x) {
        return x;
    }

    protected int calculateYManipulatingPosition(int y) {
        return y;
    }

    public void rotateRight() {
        this.boardElement.setRotationState(new RightState(boardElement));
    }

    public void rotateLeft() {
        this.boardElement.setRotationState(new LeftState(boardElement));
    }

    public void rotateUp() {
        this.boardElement.setRotationState(new UpState(boardElement));
    }

    public void rotateDown() {
        this.boardElement.setRotationState(new DownState(boardElement));
    }
}
