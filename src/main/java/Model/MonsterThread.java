package Model;

import Model.GameBoard.GameBoard;
import Model.Monsters.Monster;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Thread class used to refresh monsters movement, is not implemented by timer
 */
public class MonsterThread extends Thread {
    private static final int MONSTER_MOVE_REFRESH = 500;

    GameBoard gameBoard;
    boolean isRunning;

    public MonsterThread(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                TimeUnit.MILLISECONDS.sleep(MONSTER_MOVE_REFRESH);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<Monster> Monsters = gameBoard.getMonsters();
            for (int i = 0; i < Monsters.size(); i++) {
                moveMonster(Monsters.get(i), i, Monsters);
            }
        }
    }

    private void moveMonster(Monster monster, int i, List<Monster> Monsters) {
        boolean canUp = gameBoard.canUp(monster.getXPosition(), monster.getYPosition());
        boolean canRight = gameBoard.canRight(monster.getXPosition(), monster.getYPosition());
        boolean canDown = gameBoard.canDown(monster.getXPosition(), monster.getYPosition());
        boolean canLeft = gameBoard.canLeft(monster.getXPosition(), monster.getYPosition());

        // handle collisions with other monsters
        for (int j = 0; j < i; j++) {
            Monster loopMonster = Monsters.get(j);
            if (loopMonster.getYPosition() == monster.getYPosition() + 1 && loopMonster.getXPosition() == monster.getXPosition()) {
                canDown = false;
            } else if (loopMonster.getYPosition() == monster.getYPosition() - 1 && loopMonster.getXPosition() == monster.getXPosition()) {
                canUp = false;
            } else if (loopMonster.getYPosition() == monster.getYPosition() && loopMonster.getXPosition() == monster.getXPosition() - 1) {
                canLeft = false;
            } else if (loopMonster.getYPosition() == monster.getYPosition() && loopMonster.getXPosition() == monster.getXPosition() + 1) {
                canRight = false;
            }
        }

        monster.move(canUp, canRight, canDown, canLeft, gameBoard);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
