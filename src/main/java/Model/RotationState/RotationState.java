package Model.RotationState;

import java.awt.*;

public interface RotationState {
    int MAX_TRIALS = 3;
    void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries);
    void paint(Graphics2D g, int step, int widthPadding, int heightPadding);
    void rotateRight();
    void rotateLeft();
}
