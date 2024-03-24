package Logic;

import BoardElements.Fruit;
import BoardElements.Monsters.Monster;
import Frame.GameLabel;

public class IceManipulator{
    private final GameLabel gLabel;
    private boolean isManipulating = false;

    public IceManipulator(GameLabel gLabel){
        this.gLabel = gLabel;
    }
    public void manipulateIceAsync(){
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

        if (gLabel.getBoardArray()[playerxFreezingPosition][playeryFreezingPosition] == 1) {
            settingInt = 0;
        }

        if (gLabel.getPLAYER().getRot() == 0) {
            for (int row = playeryFreezingPosition; row >= 0; row--) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)){
                    return;
                }
                gLabel.getBoardArray()[playerxFreezingPosition][row] = settingInt;
                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 1) {
            for (int column = playerxFreezingPosition; column < gLabel.getNumOfFields(); column++) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)){
                    return;
                }
                gLabel.getBoardArray()[column][playeryFreezingPosition] = settingInt;
                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 2) {
            for (int row = playeryFreezingPosition; row < gLabel.getNumOfFields(); row++) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)){
                    return;
                }
                gLabel.getBoardArray()[playerxFreezingPosition][row] = settingInt;
                sleep(millis);
            }
        } else if (gLabel.getPLAYER().getRot() == 3) {
            for (int column = playerxFreezingPosition; column >= 0; column--) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)){
                    return;
                }
                gLabel.getBoardArray()[column][playeryFreezingPosition] = settingInt;
                sleep(millis);
            }
        }
    }
    private boolean checkIceLoop(int x, int y, int settingInt){
        if (gLabel.getBoardArray()[x][y] == settingInt || gLabel.getBoardArray()[x][y] == 2) {
            return false;
        }
        for (Monster m : gLabel.getMONSTERS()) {
            if (m.getXPosition() == x && m.getYPosition() == y) {
                return false;
            }
        }
        for (Fruit f : gLabel.getFRUIT()) {
            if(f.isTaken()) continue;
            if (f.getXPosition() == x && f.getYPosition() == y) {
                return false;
            }
        }
        return true;
    }
    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
