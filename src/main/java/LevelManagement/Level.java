package LevelManagement;

import java.util.Arrays;

public class Level {
    private final int order;
    private final int[][] GAME_BOARD;

    public int[][] getGAME_BOARD() {
        return GAME_BOARD.clone();
    }

    public Level(int[][] gameBoard, int order){
            GAME_BOARD = gameBoard;
            this.order = order;
    }

    @Override
    public String toString() {
        return "Order: " + this.order + System.lineSeparator() + " Game Board: " + Arrays.deepToString(this.GAME_BOARD) + System.lineSeparator();
    }
}
