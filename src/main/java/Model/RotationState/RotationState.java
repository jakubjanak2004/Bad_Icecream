package Model.RotationState;

import java.awt.*;

public abstract class RotationState {
    protected static final int MAX_TRIALS = 3;
    abstract public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries);
    abstract public void paint(Graphics2D g, int step, int widthPadding, int heightPadding);
    abstract public void manipulateIce();

    protected static void sleep() {
        try {
            Thread.sleep(Model.GameBoard.IceManipulator.MILLIS_WAIT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
