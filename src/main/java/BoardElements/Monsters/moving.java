package BoardElements.Monsters;

import Logic.GameController;

/**
 * Represents an object that moves itself when selfMove is called.
 */
public interface moving {
    /**
     * Will move itself and use appropriate algorithm.
     * @param canUp can the object go up
     * @param canRight can the object go right
     * @param canDown can the monster go down
     * @param canLeft can the monster go left
     * @param gameController the game Controller on which the game is running
     */
    void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController);

    /**
     * The object must have an x position
     * @return integer x position
     */
    int getXPosition();

    /**
     * The object must have a y position
     * @return integer y position
     */
    int getYPosition();
}
