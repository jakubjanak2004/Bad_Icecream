package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import Logic.GameController;
import Logic.ShortestPath;

import java.awt.*;
import java.util.Optional;

/**
 * This class represents a monster that is clever and can find the player even if it is hiding.
 */
public class CleverMonster extends Monster {
    public CleverMonster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
        super.color = Color.RED;
    }

    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        Optional<BoardElement>[][] boardElements = gameController.getBoardArrayObject();

        Rotation rot = ShortestPath.getShortestPathWithIceStart(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), boardElements);

        if (rot != Rotation.NEUTRAL) {
            setRot(rot);
        }
        this.getRotationState().move(canUp, canRight, canDown, canLeft, 0);
    }
}
