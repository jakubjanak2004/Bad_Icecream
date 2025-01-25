package BoardElements.GameBoard;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Monsters.CleverMonster;
import BoardElements.Monsters.StrongMonster;
import BoardElements.Monsters.StupidMonster;
import BoardElements.Monsters.moving;
import BoardElements.Player;
import BoardElements.Reward.Chest;
import BoardElements.Reward.Fruit;
import BoardElements.Reward.Key;
import BoardElements.Reward.Reward;
import BoardElements.Rotation;
import Logic.IceManipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameBoardBuilder {
    private Optional<BoardElement>[][] boardElementArray;
    private final Player player;
    private final List<moving> monsters = Collections.synchronizedList(new ArrayList<>());;
    private final List<Reward> rewards = Collections.synchronizedList(new ArrayList<>());;

    public GameBoardBuilder() {
        this.player = new Player(0, 0, Rotation.UP);
    }

    public static GameBoardBuilder builder(){
        return new GameBoardBuilder();
    }

    // TODO: refactor as is not readable
    public GameBoardBuilder setBoardElementArray(int[][] intGameBoardArray){
        this.boardElementArray = GameBoardBuilder.createEmptyArray(intGameBoardArray.length, intGameBoardArray[0].length);
        List<Chest> chests = new ArrayList<>();
        List<Key> keys = new ArrayList<>();
        for (int i = 0; i < intGameBoardArray.length; i++) {
            for (int j = 0; j < intGameBoardArray[i].length; j++) {
                if (intGameBoardArray[i][j] == -1) {
                    player.setXPosition(i);
                    player.setYPosition(j);
                } else if (intGameBoardArray[i][j] == 3) {
                    monsters.add(new StupidMonster(i, j, Rotation.UP));
                } else if (intGameBoardArray[i][j] == 4) {
                    monsters.add(new CleverMonster(i, j, Rotation.UP));
                } else if (intGameBoardArray[i][j] == 5) {
                    monsters.add(new StrongMonster(i, j, Rotation.UP));
                } else if (intGameBoardArray[i][j] == 6) {
                    rewards.add(new Fruit(i, j));
                } else if (intGameBoardArray[i][j] == 7) {
                    Chest chest = new Chest(i, j);
                    rewards.add(chest);
                    chests.add(chest);
                } else if (intGameBoardArray[i][j] == 8) {
                    Key key = new Key(i, j);
                    rewards.add(key);
                    keys.add(key);
                } else if (intGameBoardArray[i][j] == 1) {
                    this.boardElementArray[i][j] =  Optional.of(new IceBlock(i, j));
                } else if (intGameBoardArray[i][j] == 2) {
                    this.boardElementArray[i][j] =  Optional.of(new SolidBlock(i, j));
                } else if (intGameBoardArray[i][j] == 0) {
                    this.boardElementArray[i][j] =  Optional.of(new BoardElement(i, j));
                }
            }
        }

        for (Chest chest : chests) {
            chest.setKeys(keys);
        }

        return this;
    }

    public GameBoard build(){
        GameBoard gameBoard = new GameBoard(boardElementArray, rewards, monsters);
        gameBoard.setPlayer(player);
        gameBoard.setBoardElementArray(boardElementArray);
        return gameBoard;
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
