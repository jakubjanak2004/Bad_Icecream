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
    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        Optional<BoardElement>[][] boardElements = gameController.getGameBoard().getBoardElementArray();

        Rotation rot = ShortestPath.getPathStartNoIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), boardElements);

        setRot(rot);

        return true;
    }
}
