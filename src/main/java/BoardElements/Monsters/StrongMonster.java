package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import Logic.GameController;
import Logic.ShortestPath;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

/**
 * Strong monster represents a monster that is strong and can break through ice.
 */
public class StrongMonster extends Monster {
    public StrongMonster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
        super.color = Color.ORANGE;
    }

    @Override
    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        Optional<BoardElement>[][] boardElements = gameController.getBoardArrayObject();

        Rotation rotation = ShortestPath.getPathStartWithIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), boardElements);

        // Mapping rotations to corresponding actions
        Map<Rotation, Runnable> actions = Map.of(
                Rotation.UP, () -> handleIce(gameController, 0, -1),
                Rotation.RIGHT, () -> handleIce(gameController, 1, 0),
                Rotation.DOWN, () -> handleIce(gameController, 0, 1),
                Rotation.LEFT, () -> handleIce(gameController, -1, 0)
        );

        if (actions.containsKey(rotation) && gameController.isFrozenAtLoc(getDeltaX(rotation), getDeltaY(rotation), this)) {
            actions.get(rotation).run();
            return false;
        }

        setRot(rotation);

        return true;
    }

    private void handleIce(GameController gameController, int deltaX, int deltaY) {
        gameController.beatIce(getXPosition() + deltaX, getYPosition() + deltaY);
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
