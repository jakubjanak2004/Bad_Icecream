package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Reward.Reward;
import BoardElements.Monsters.SelfMovable;

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
        int playerxFreezingPosition = gLabel.getPLAYER().getXPosition();
        int playeryFreezingPosition = gLabel.getPLAYER().getYPosition();
        int settingInt = 1;
        int millis = 100;

        switch (gLabel.getPLAYER().getRot()) {
            case 0:
                playeryFreezingPosition -= 1;
                break;
            case 1:
                playerxFreezingPosition += 1;
                break;
            case 2:
                playeryFreezingPosition += 1;
                break;
            case 3:
                playerxFreezingPosition -= 1;
                break;
        }

        if (playerxFreezingPosition < 0 || playerxFreezingPosition >= gLabel.getNumOfFields()) {
            return;
        }
        if (playeryFreezingPosition < 0 || playeryFreezingPosition >= gLabel.getNumOfFields()) {
            return;
        }

        if (gLabel.getBoardArrayObject()[playerxFreezingPosition][playeryFreezingPosition] != null) {
            if (gLabel.getBoardArrayObject()[playerxFreezingPosition][playeryFreezingPosition].getClass() != BoardElement.class) {
                settingInt = 0;
            }
        }

        if (gLabel.getPLAYER().getRot() == 0) {
            for (int row = playeryFreezingPosition; row >= 0; row--) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 1) {
            for (int column = playerxFreezingPosition; column < gLabel.getNumOfFields(); column++) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 2) {
            for (int row = playeryFreezingPosition; row < gLabel.getNumOfFields(); row++) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 3) {
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
        if (gLabel.getBoardArrayObject()[x][y] != null && gLabel.getBoardArrayObject()[x][y].getClass() == IceBlock.class && settingInt == 0) {
            //gLabel.getBoardArrayObject()[x][y] = null;
            BoardElement replacement = new BoardElement(x, y);
            gLabel.getBoardArrayObject()[x][y] = replacement;
        } else {
            gLabel.getBoardArrayObject()[x][y] = new IceBlock(x, y);
        }
    }

    private boolean checkIceLoop(int x, int y, int settingInt) {
        // checking if next tile is or is not an ice, according to the settingInt
        if (settingInt == 0) {
            if (gLabel.getBoardArrayObject()[x][y] == null || gLabel.getBoardArrayObject()[x][y].getClass() == SolidBlock.class
                    || gLabel.getBoardArrayObject()[x][y].getClass() == BoardElement.class) {
                return false;
            }
        } else if (settingInt == 1) {
            if (gLabel.getBoardArrayObject()[x][y] != null) {
                if (gLabel.getBoardArrayObject()[x][y].getClass() != BoardElement.class) {
                    return false;
                }
            }
        }

        for (SelfMovable m : gLabel.getMONSTERS()) {
            if (m.getXPosition() == x && m.getYPosition() == y) {
                return false;
            }
        }
        for (Reward f : gLabel.getREWARD()) {
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
