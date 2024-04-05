package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.Fruit.Fruit;
import BoardElements.Monsters.Monster;

public class IceManipulator {
    private final GameController gLabel;
    private boolean isManipulating = false;

    public IceManipulator(GameController gLabel) {
        this.gLabel = gLabel;
    }

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

//        if (gLabel.getBoardArray()[playerxFreezingPosition][playeryFreezingPosition] == 1) {
//            settingInt = 0;
//        }
        if (gLabel.getBoardArrayObject()[playerxFreezingPosition][playeryFreezingPosition] != null) {
            settingInt = 0;
        }

        if (gLabel.getPLAYER().getRot() == 0) {
            for (int row = playeryFreezingPosition; row >= 0; row--) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

//                gLabel.getBoardArray()[playerxFreezingPosition][row] = settingInt;

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 1) {
            for (int column = playerxFreezingPosition; column < gLabel.getNumOfFields(); column++) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

//                gLabel.getBoardArray()[column][playeryFreezingPosition] = settingInt;

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 2) {
            for (int row = playeryFreezingPosition; row < gLabel.getNumOfFields(); row++) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

//                gLabel.getBoardArray()[playerxFreezingPosition][row] = settingInt;

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 3) {
            for (int column = playerxFreezingPosition; column >= 0; column--) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

//                gLabel.getBoardArray()[column][playeryFreezingPosition] = settingInt;

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        }
    }

    private void changeArray(int x, int y, int settingInt) {
        if (gLabel.getBoardArrayObject()[x][y] != null && gLabel.getBoardArrayObject()[x][y].getClass() == IceBlock.class && settingInt == 0) {
            gLabel.getBoardArrayObject()[x][y] = null;
        } else {
            gLabel.getBoardArrayObject()[x][y] = new IceBlock(x, y);
        }
    }

    private boolean checkIceLoop(int x, int y, int settingInt) {

        // checking for ice blocks and solid blocks to stop the ice loop
//        if (gLabel.getBoardArray()[x][y] == settingInt || gLabel.getBoardArray()[x][y] == 2) {
//            return false;
//        }

        // checking if next tile is or is not an ice, according to the settingInt
        if (settingInt == 0) {
            if (gLabel.getBoardArrayObject()[x][y] == null || gLabel.getBoardArrayObject()[x][y].getClass() == SolidBlock.class) {
                return false;
            }
        } else if (settingInt == 1) {
            if (gLabel.getBoardArrayObject()[x][y] != null) {
                return false;
            }
        }

        for (Monster m : gLabel.getMONSTERS()) {
            if (m.getXPosition() == x && m.getYPosition() == y) {
                return false;
            }
        }
        for (Fruit f : gLabel.getFRUIT()) {
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
