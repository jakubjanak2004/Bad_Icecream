package Model.GameBoard;

import Model.Blocks.IceBlock;
import Model.Blocks.SolidBlock;
import Model.BoardElement.BoardElement;
import Model.Monster.CleverMonster;
import Model.Monster.Monster;
import Model.Monster.StrongMonster;
import Model.Monster.StupidMonster;
import Model.Player.Player;
import Model.Reward.Chest;
import Model.Reward.Fruit;
import Model.Reward.Key;
import Model.Reward.Reward;
import Model.Player.Rotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameBoardBuilder {
    GameBoard gameBoard;
    private Optional<BoardElement>[][] boardElementArray;
    private int[][] intGameBoardArray;
    private Player player;
    private final List<Monster> monsters = Collections.synchronizedList(new ArrayList<>());;
    private final List<Reward> rewards = Collections.synchronizedList(new ArrayList<>());;

    public static GameBoardBuilder builder(){
        return new GameBoardBuilder();
    }

    public GameBoardBuilder setBoardElementArray(int[][] intGameBoardArray){
        this.intGameBoardArray = intGameBoardArray;
        return this;
    }

    public GameBoard build(){
        gameBoard = new GameBoard(boardElementArray, rewards, monsters);
        this.player = new Player(0, 0, Rotation.UP, gameBoard);
        gameBoard.setPlayer(player);
        constructBoardElementArray();
        gameBoard.setBoardElementArray(boardElementArray);
        return gameBoard;
    }

    // TODO: refactor as is not readable
    private void constructBoardElementArray() {
        this.boardElementArray = GameBoardBuilder.createEmptyArray(intGameBoardArray.length, intGameBoardArray[0].length);
        List<Chest> chests = new ArrayList<>();
        List<Key> keys = new ArrayList<>();
        for (int i = 0; i < intGameBoardArray.length; i++) {
            for (int j = 0; j < intGameBoardArray[i].length; j++) {
                if (intGameBoardArray[i][j] == -1) {
                    player.setXPosition(i);
                    player.setYPosition(j);
                } else if (intGameBoardArray[i][j] == 3) {
                    monsters.add(new StupidMonster(i, j, Rotation.UP, gameBoard));
                } else if (intGameBoardArray[i][j] == 4) {
                    monsters.add(new CleverMonster(i, j, Rotation.UP, gameBoard));
                } else if (intGameBoardArray[i][j] == 5) {
                    monsters.add(new StrongMonster(i, j, Rotation.UP, gameBoard));
                } else if (intGameBoardArray[i][j] == 6) {
                    rewards.add(new Fruit(i, j, gameBoard));
                } else if (intGameBoardArray[i][j] == 7) {
                    Chest chest = new Chest(i, j, gameBoard);
                    rewards.add(chest);
                    chests.add(chest);
                } else if (intGameBoardArray[i][j] == 8) {
                    Key key = new Key(i, j, gameBoard);
                    rewards.add(key);
                    keys.add(key);
                } else if (intGameBoardArray[i][j] == 1) {
                    this.boardElementArray[i][j] =  Optional.of(new IceBlock(i, j, gameBoard));
                } else if (intGameBoardArray[i][j] == 2) {
                    this.boardElementArray[i][j] =  Optional.of(new SolidBlock(i, j, gameBoard));
                } else if (intGameBoardArray[i][j] == 0) {
                    this.boardElementArray[i][j] =  Optional.of(new BoardElement(i, j, gameBoard));
                }
            }
        }

        for (Chest chest : chests) {
            chest.setKeys(keys);
        }
    }

    private static Optional<BoardElement>[][] createEmptyArray(int rows, int columns) {
        Optional<BoardElement>[][] array = new Optional[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i][j] = Optional.empty();
            }
        }
        return array;
    }
}
