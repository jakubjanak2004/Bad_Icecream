package Model.GameBoard;

import Model.Blocks.IceBlock;
import Model.Blocks.SolidBlock;
import Model.BoardElement.BoardElement;
import Model.Monsters.Monster;
import Model.Reward.Reward;
import Model.Monsters.Movable;
import Model.RotationState.DownState;
import Model.RotationState.LeftState;
import Model.RotationState.RightState;
import Model.RotationState.UpState;

import java.util.Optional;

/**
 * Ice Manipulator class is used for manipulating the ice, it is used by player and monsters.
 */
public class IceManipulator {
    private final GameBoard gameBoard;
    private boolean isManipulating = false;

    public IceManipulator(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
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
        int playerxFreezingPosition = gameBoard.getPlayer().getXPosition();
        int playeryFreezingPosition = gameBoard.getPlayer().getYPosition();
        int settingInt = 1;
        int millis = 100;

        if (gameBoard.getPlayer().getRotationState().getClass().equals(UpState.class)) {
            playeryFreezingPosition -= 1;
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(RightState.class)) {
            playerxFreezingPosition += 1;
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(DownState.class)) {
            playeryFreezingPosition += 1;
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(LeftState.class)) {
            playerxFreezingPosition -= 1;
        }

        if (playerxFreezingPosition < 0 || playerxFreezingPosition >= gameBoard.getBoardElementArray().length) {
            return;
        }
        if (playeryFreezingPosition < 0 || playeryFreezingPosition >= gameBoard.getBoardElementArray()[0].length) {
            return;
        }

        if (gameBoard.getBoardElementArray()[playerxFreezingPosition][playeryFreezingPosition].isPresent()) {
            if (gameBoard.getBoardElementArray()[playerxFreezingPosition][playeryFreezingPosition].get().getClass() != BoardElement.class) {
                settingInt = 0;
            }
        }

        if (gameBoard.getPlayer().getRotationState().getClass().equals(UpState.class)) {
            for (int row = playeryFreezingPosition; row >= 0; row--) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(RightState.class)) {
            for (int column = playerxFreezingPosition; column < gameBoard.getBoardElementArray().length; column++) {
                if (!checkIceLoop(column, playeryFreezingPosition, settingInt)) {
                    return;
                }

                changeArray(column, playeryFreezingPosition, settingInt);

                sleep(millis);
            }
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(DownState.class)) {
            for (int row = playeryFreezingPosition; row < gameBoard.getBoardElementArray().length; row++) {
                if (!checkIceLoop(playerxFreezingPosition, row, settingInt)) {
                    return;
                }

                changeArray(playerxFreezingPosition, row, settingInt);

                sleep(millis);
            }
        } else if (gameBoard.getPlayer().getRotationState().getClass().equals(LeftState.class)) {
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
        if (gameBoard.getBoardElementArray()[x][y].isPresent() && gameBoard.getBoardElementArray()[x][y].get().getClass() == IceBlock.class && settingInt == 0) {
            BoardElement replacement = new BoardElement(x, y, gameBoard);
            gameBoard.getBoardElementArray()[x][y] = Optional.of(replacement);
        } else {
            gameBoard.getBoardElementArray()[x][y] = Optional.of(new IceBlock(x, y, gameBoard));
        }
    }

    private boolean checkIceLoop(int x, int y, int settingInt) {
        // checking if next tile is or is not an ice, according to the settingInt
        if (settingInt == 0) {
            if (gameBoard.getBoardElementArray()[x][y].isEmpty() || gameBoard.getBoardElementArray()[x][y].get().getClass() == SolidBlock.class
                    || gameBoard.getBoardElementArray()[x][y].get().getClass() == BoardElement.class) {
                return false;
            }
        } else if (settingInt == 1) {
            if (gameBoard.getBoardElementArray()[x][y].isPresent()) {
                if (gameBoard.getBoardElementArray()[x][y].get().getClass() != BoardElement.class) {
                    return false;
                }
            }
        }

        for (Monster m : gameBoard.getMonsters()) {
            if (m.getXPosition() == x && m.getYPosition() == y) {
                return false;
            }
        }
        for (Reward f : gameBoard.getRewards()) {
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
