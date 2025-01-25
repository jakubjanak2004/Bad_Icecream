package BoardElements.GameBoard;

import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement;
import BoardElements.Monsters.Monster;
import BoardElements.Monsters.moving;
import BoardElements.Player;
import BoardElements.Reward.Reward;
import BoardElements.Rotation;
import Logic.IceManipulator;

import java.util.List;
import java.util.Optional;

public class GameBoard {
    private final IceManipulator iceManipulator;
    private final List<moving> monsters;
    private final List<Reward> rewards;
    private Player player;
    private Optional<BoardElement>[][] boardElementArray;

    public GameBoard(Optional<BoardElement>[][] boardElementArray, List<Reward> rewards, List<moving> monsters) {
        this.boardElementArray = boardElementArray;
        this.monsters = monsters;
        this.rewards = rewards;
        this.iceManipulator = new IceManipulator(this);
        this.player = new Player(0, 0, Rotation.UP);
    }

    public boolean isVisitable(int x, int y) {
        if (isOutsideOfBoard(x, y)) return false;

        if (boardElementArray[x][y].isPresent()) {
            return boardElementArray[x][y].map(BoardElement::isVisitable).orElse(false);
        }
        return true;
    }

    private boolean isOutsideOfBoard(int x, int y) {
        if (x < 0 || x >= boardElementArray.length) {
            return true;
        }
        return y < 0 || y >= boardElementArray[0].length;
    }

    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return (boardElementArray[monster.getXPosition() + xMove][monster.getYPosition() + yMove].isPresent()
                && boardElementArray[monster.getXPosition() + xMove][monster.getYPosition() + yMove].get().getClass() == IceBlock.class);
    }

    public void beatIce(int x, int y) {
        if (isOutsideOfBoard(x, y)) return;

        if (boardElementArray[x][y].isPresent() && boardElementArray[x][y].get().getClass() == IceBlock.class) {
            IceBlock iceBlock = (IceBlock) boardElementArray[x][y].get();
            iceBlock.destabilize();

            if (iceBlock.getStability() <= 0) {
                BoardElement replacement = new BoardElement(x, y);
                boardElementArray[x][y] = Optional.of(replacement);
            }
        }
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

    public boolean checkDeath() {
        for (moving m : monsters) {
            if (Math.abs(m.getXPosition() - player.getXPosition()) <= 1 && m.getYPosition() == player.getYPosition()) {
                return true;
            }
            if (Math.abs(m.getYPosition() - player.getYPosition()) <= 1 && m.getXPosition() == player.getXPosition()) {
                return true;
            }
        }
        return false;
    }

    public Optional<BoardElement>[][] getBoardElementArray() {
        return boardElementArray;
    }

    public void setBoardElementArray(Optional<BoardElement>[][] boardElementArray) {
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

    public List<moving> getMonsters() {
        return monsters;
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
