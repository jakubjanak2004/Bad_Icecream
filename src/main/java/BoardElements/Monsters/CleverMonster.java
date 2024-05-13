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
    public void selfMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        Optional[][] boardElements = gameController.getBoardArrayObject();

        String shortestPath = ShortestPath.getShortestMazePathStart(getXPosition(), getYPosition(), player.getXPosition(),
                player.getYPosition(), "", 's', boardElements, boardElements.length);

        if (!shortestPath.isEmpty()) {
            moveTo(shortestPath.charAt(0));
        } else {
            move(canUp, canRight, canDown, canLeft, 0);
        }
    }
}
