package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Reward.Reward;
import BoardElements.Monsters.SelfMovable;
import BoardElements.Rotation;

import java.util.Optional;

/**
 * Ice Manipulator class is used for manipulating the ice, it is used by player and monsters.
 */
public class IceManipulator {
    private final GameController gLabel;
    private boolean isManipulating = false;

    public IceManipulator(GameController gLabel) {
        this.gLabel = gLabel;
    }

    /**
     * Manipulate Ice and while manipulating cannot start another manipulation,
     * is used for player block freezing/unfreezing
     */
    public void manipulateIceAsync() {
        if (isManipulating) return;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                isManipulating = true;
                manipulateIce();
                isManipulating = false;
            }
        });
        t1.start();
    }

    /**
     * Manipulating the ice, used by monsters to break ice slowly.
     */
    public void manipulateIce() {
        int playerxFreezingPosition = gLabel.getPlayer().getXPosition();
        int playeryFreezingPosition = gLabel.getPlayer().getYPosition();
        int settingInt = 1;
        int millis = 100;

        switch (gLabel.getPlayer().getRot()) {
            case UP:
                playeryFreezingPosition -= 1;
                break;
            case RIGHT:
                playerxFreezingPosition += 1;
                break;
            case DOWN:
                playeryFreezingPosition += 1;
                break;
            case LEFT:
                playerxFreezingPosition -= 1;
                break;
        }

        if (playerxFreezingPosition < 0 || playerxFreezingPosition >= gLabel.getNumOfFields()) {
            return;
        }
        if (playeryFreezingPosition < 0 || playeryFreezingPosition >= gLabel.getNumOfFields()) {
            return;
        }

        if (gLabel.getBoardArrayObject()[playerxFreezingPosition][playeryFreezingPosition].isPresent()) {
            if (gLabel.getBoardArrayObject()[playerxFreezingPosition][playeryFreezingPosition].get().getClass() != BoardElement.class) {
                settingInt = 0;
            }
        }

        if (gLabel.getPlayer().getRot() == Rotation.UP) {
            for (int row = playeryFreezingPosition; row >= 0; row--) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPlayer().getRot() == Rotation.RIGHT) {
            for (int column = playerxFreezingPosition; column < gLabel.getNumOfFields(); column++) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPlayer().getRot() == Rotation.DOWN) {
            for (int row = playeryFreezingPosition; row < gLabel.getNumOfFields(); row++) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPlayer().getRot() == Rotation.LEFT) {
            for (int column = playerxFreezingPosition; column >= 0; column--) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        }
    }

    private void changeArray(int x, int y, int settingInt) {
        if (gLabel.getBoardArrayObject()[x][y].isPresent() && gLabel.getBoardArrayObject()[x][y].get().getClass() == IceBlock.class && settingInt == 0) {
            //gLabel.getBoardArrayObject()[x][y] = null;
            BoardElement replacement = new BoardElement(x, y);
            gLabel.getBoardArrayObject()[x][y] = Optional.of(replacement);
        } else {
            gLabel.getBoardArrayObject()[x][y] = Optional.of(new IceBlock(x, y));
        }
    }

    private boolean checkIceLoop(int x, int y, int settingInt) {
        // checking if next tile is or is not an ice, according to the settingInt
        if (settingInt == 0) {
            if (gLabel.getBoardArrayObject()[x][y].isEmpty() || gLabel.getBoardArrayObject()[x][y].get().getClass() == SolidBlock.class
                    || gLabel.getBoardArrayObject()[x][y].get().getClass() == BoardElement.class) {
                return false;
            }
        } else if (settingInt == 1) {
            if (gLabel.getBoardArrayObject()[x][y].isPresent()) {
                if (gLabel.getBoardArrayObject()[x][y].get().getClass() != BoardElement.class) {
                    return false;
                }
            }
        }

        for (SelfMovable m : gLabel.getMonsters()) {
            if (m.getXPosition() == x && m.getYPosition() == y) {
                return false;
            }
        }
        for (Reward f : gLabel.getRewards()) {
            if (f.isTaken()) continue;
            if (f.getXPosition() == x && f.getYPosition() == y) {
                return false;
            }
        }
        return true;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
