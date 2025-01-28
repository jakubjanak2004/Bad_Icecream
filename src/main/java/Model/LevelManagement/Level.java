package Model.LevelManagement;

import java.util.Arrays;

/**
 * Level class represents a level that is being downloaded from the levels folder, it is therefore a object oriented way of representing a level.
 */
public class Level {
    private final int order;
    private final int[][] gameBoard;

    public Level(int[][] gameBoard, int order){
            this.gameBoard = gameBoard;
            this.order = order;
    }

    /**
     * Will convert this object into a string if needed, for example for logging purposes.
     * @return String representation of this object.
     */
    @Override
    public String toString() {
        return "Order: " + this.order + System.lineSeparator() + " Game Board: " + Arrays.deepToString(this.gameBoard) + System.lineSeparator();
    }

    public int[][] getGameBoardClone() {
        return gameBoard.clone();
    }
}
