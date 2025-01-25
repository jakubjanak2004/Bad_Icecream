package Model.Monsters;

import Model.GameBoard.GameBoard;

/**
 * Represents an object that moves itself when selfMove is called.
 */
public interface movable {
    /**
     * Will move itself and use appropriate algorithm.
     * @param canUp can the object go up
     * @param canRight can the object go right
     * @param canDown can the monster go down
     * @param canLeft can the monster go left
     * @param GameBoard
     */
    void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard);

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
