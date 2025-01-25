package Model.Monster;

import Model.GameBoard.GameBoard;
import Model.Player.Player;
import Model.Player.Rotation;
import Model.ShortestPath.ShortestPath;

import java.awt.*;
import java.util.Map;

/**
 * Strong monster represents a monster that is strong and can break through ice.
 */
public class StrongMonster extends Monster {
    public StrongMonster(int xPosition, int yPosition, Rotation rot, GameBoard gameBoard) {
        super(xPosition, yPosition, rot, gameBoard);
        super.color = Color.ORANGE;
    }

    @Override
    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        Player player = gameBoard.getPlayer();

        Rotation rotation = ShortestPath.getPathStartWithIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), gameBoard.getBoardElementArray());

        // Mapping rotations to corresponding actions
        Map<Rotation, Runnable> actions = Map.of(
                Rotation.UP, () -> handleIce(gameBoard, 0, -1),
                Rotation.RIGHT, () -> handleIce(gameBoard, 1, 0),
                Rotation.DOWN, () -> handleIce(gameBoard, 0, 1),
                Rotation.LEFT, () -> handleIce(gameBoard, -1, 0)
        );

        if (actions.containsKey(rotation) && gameBoard.isFrozenAtLoc(getDeltaX(rotation), getDeltaY(rotation), this)) {
            actions.get(rotation).run();
            return false;
        }

        setRot(rotation);

        return true;
    }

    private void handleIce(GameBoard gameBoard, int deltaX, int deltaY) {
        gameBoard.beatIce(getXPosition() + deltaX, getYPosition() + deltaY);
    }

    private int getDeltaX(Rotation rotation) {
        return switch (rotation) {
            case RIGHT -> 1;
            case LEFT -> -1;
            default -> 0;
        };
    }

    private int getDeltaY(Rotation rotation) {
        return switch (rotation) {
            case UP -> -1;
            case DOWN -> 1;
            default -> 0;
        };
    }
}
