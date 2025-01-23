package Logic;

import BoardElements.Monsters.Monster;
import BoardElements.Monsters.moving;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Thread class used to refresh monsters movement, is not implemented by timer
 */
public class MonsterThread extends Thread {
    GameController gameController;
    boolean isRunning;

    public MonsterThread(GameController gameController) {
        this.gameController = gameController;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                TimeUnit.MILLISECONDS.sleep(gameController.getMONSTER_MOVE_REFRESH());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<moving> Monsters = gameController.getMonsters();
            for (int i = 0; i < Monsters.size(); i++) {

                if (!(Monsters.get(i) instanceof Monster monster)) {
                    continue;
                }

                moveMonster(monster, i, Monsters);
            }
        }
    }

    private void moveMonster(Monster monster, int i, List<moving> Monsters) {
        boolean canUp = gameController.canUp(monster.getXPosition(), monster.getYPosition());
        boolean canRight = gameController.canRight(monster.getXPosition(), monster.getYPosition());
        boolean canDown = gameController.canDown(monster.getXPosition(), monster.getYPosition());
        boolean canLeft = gameController.canLeft(monster.getXPosition(), monster.getYPosition());

        // handle collisions with other monsters
        for (int j = 0; j < i; j++) {
            Monster loopMonster = (Monster) Monsters.get(j);
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

        monster.move(canUp, canRight, canDown, canLeft, gameController);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
