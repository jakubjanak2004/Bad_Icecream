package Model.GameBoard;

import Model.Block.IceBlock;
import Model.BoardElement.BoardElement;
import Model.Monster.Monster;
import Model.MonsterThread;
import Model.Player.Player;
import Model.Player.Rotation;
import Model.Reward.Reward;

import java.util.List;
import java.util.Optional;

public class GameBoard {
    private final IceManipulator iceManipulator;
    private final List<Monster> monsters;
    private final List<Reward> rewards;
    private MonsterThread monsterThread;
    private Player player;
    private BoardElement[][] boardElementArray;

    public GameBoard(BoardElement[][] boardElementArray, List<Reward> rewards, List<Monster> monsters) {
        this.boardElementArray = boardElementArray;
        this.monsters = monsters;
        this.rewards = rewards;
        this.monsterThread = new MonsterThread(this);
        this.iceManipulator = new IceManipulator(this);
        this.player = new Player(0, 0, Rotation.UP, this);
    }

    public void setBoardElementAt(int xPosition, int yPosition, BoardElement newElement) {
        boardElementArray[xPosition][yPosition] = newElement;
    }

    public Optional<BoardElement> getBoardElementAt(int x, int y) {
        return Optional.ofNullable(boardElementArray[x][y]);
    }

    public boolean isVisitable(int x, int y) {
        if (isOutsideOfBoard(x, y)) return false;

        // new code
        return getBoardElementAt(x, y)
                .map(BoardElement::isVisitable)
                .orElse(false);
    }

    private boolean isOutsideOfBoard(int x, int y) {
        if (x < 0 || x >= boardElementArray.length) {
            return true;
        }
        return y < 0 || y >= boardElementArray[0].length;
    }

    public boolean isFrozenAtLoc(int x, int y) {
        return getBoardElementAt(x, y)
                .filter(boardElement -> boardElement.getClass() == IceBlock.class)
                .isPresent();
    }

    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return getBoardElementAt(monster.getXPosition() + xMove, monster.getYPosition() + yMove)
                .filter(boardElement -> boardElement.getClass() == IceBlock.class)
                .isPresent();
    }

    public void beatIce(int x, int y) {
        if (isOutsideOfBoard(x, y)) return;

        getBoardElementAt(x, y)
                .filter(boardElement -> boardElement instanceof IceBlock)
                .map(boardElement -> (IceBlock) boardElement)
                .ifPresent(IceBlock::destabilize);
    }

    public void checkFruitTaken() {
        for (Reward f : rewards) {
            f.tryGrabbing(player);
        }
    }

    public boolean checkAllFruitGone() {
        for (Reward f : rewards) {
            if (f.isTaken()) continue;
            return false;
        }
        return true;
    }

    public void startGame() {
        monsterThread = new MonsterThread(this);
        monsterThread.start();
    }

    public void stopGame() {
        monsterThread.setRunning(false);
    }

    public boolean checkDeath() {
        return monsters.stream().anyMatch(monster -> monster.tryKilling(player));
    }

    public boolean canUp(int x, int y) {
        return y > 0 && isVisitable(x, y - 1);
    }

    public boolean canRight(int x, int y) {
        return x < boardElementArray.length - 1 && isVisitable(x + 1, y);
    }

    public boolean canDown(int x, int y) {
        return y < boardElementArray[0].length && isVisitable(x, y + 1);
    }

    public boolean canLeft(int x, int y) {
        return x > 0 && isVisitable(x - 1, y);
    }

    public int getGameBoardLengthX() {
        return boardElementArray.length;
    }

    public int getGameBoardLengthY() {
        return boardElementArray[0].length;
    }

    public void setBoardElementArray(BoardElement[][] boardElementArray) {
        this.boardElementArray = boardElementArray;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public IceManipulator getIceManipulator() {
        return iceManipulator;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
