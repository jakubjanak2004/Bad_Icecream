package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import Logic.GameController;
import Logic.ShortestPath;

import java.awt.*;
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
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        Optional<BoardElement>[][] boardElements = gameController.getBoardArrayObject();

        Rotation rotation = ShortestPath.getPathStartWithIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), boardElements);

        if (rotation == Rotation.UP && gameController.isFrozenAtLoc(0, -1, this)) {
            gameController.beatIce(getXPosition(), getYPosition() - 1);
            return;
        } else if (rotation == Rotation.RIGHT && gameController.isFrozenAtLoc(1, 0, this)) {
            gameController.beatIce(getXPosition() + 1, getYPosition());
            return;
        } else if (rotation == Rotation.DOWN && gameController.isFrozenAtLoc(0, 1, this)) {
            gameController.beatIce(getXPosition(), getYPosition() + 1);
            return;
        } else if (rotation == Rotation.LEFT && gameController.isFrozenAtLoc(-1, 0, this)) {
            gameController.beatIce(getXPosition() - 1, getYPosition());
            return;
        }

        if (rotation != Rotation.NEUTRAL) {
            setRot(rotation);
        }
        this.getRotationState().move(canUp, canRight, canDown, canLeft, 0);
    }
}
