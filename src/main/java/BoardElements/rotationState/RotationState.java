package BoardElements.rotationState;

public interface RotationState {
    int MAX_TRIALS = 3;
    void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries);
    void rotateRight();
    void rotateLeft();
}
