package Model.GameBoard;

import Model.Block.IceBlock;
import Model.Block.SolidBlock;
import Model.BoardElement.BoardElement;
import Model.Monster.Monster;
import Model.Reward.Reward;
import Model.RotationState.DownState;
import Model.RotationState.LeftState;
import Model.RotationState.RightState;
import Model.RotationState.UpState;

/**
 * Ice Manipulator class is used for manipulating the ice, it is used by player and monsters.
 */
public class IceManipulator {
    private final GameBoard gameBoard;
    public static final int MILLIS_WAIT = 100;
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

        Thread.ofVirtual().start(() -> {
            isManipulating = true;
            gameBoard.getPlayer().getRotationState().manipulateIce();
            isManipulating = false;
        });
    }

    public static void changeArray(int x, int y, boolean freeze, GameBoard gameBoard) {
        if (gameBoard.getBoardElementAt(x, y).isPresent() && gameBoard.getBoardElementAt(x, y).get().getClass() == IceBlock.class && !freeze) {
            BoardElement replacement = new BoardElement(x, y, gameBoard);
            gameBoard.setBoardElementAt(x, y, replacement);
        } else {
            gameBoard.setBoardElementAt(x, y, new IceBlock(x, y, gameBoard));
        }
    }

    public static boolean checkIceLoop(int x, int y, boolean freeze, GameBoard gameBoard) {
        // checking if next tile is or is not an ice, according to the settingInt
        if (!freeze) {
            if (gameBoard.getBoardElementAt(x, y).isEmpty() || gameBoard.getBoardElementAt(x, y).get().getClass() == SolidBlock.class
                    || gameBoard.getBoardElementAt(x, y).get().getClass() == BoardElement.class) {
                return false;
            }
        } else {
            if (gameBoard.getBoardElementAt(x, y).isPresent()) {
                if (gameBoard.getBoardElementAt(x, y).get().getClass() != BoardElement.class) {
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
}
