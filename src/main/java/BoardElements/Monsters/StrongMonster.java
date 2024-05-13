package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import Logic.GameController;
import Logic.ShortestPath;

import java.awt.*;

/**
 * Strong monster represents a monster that is strong and can break through ice.
 */
public class StrongMonster extends Monster {
    public StrongMonster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
        super.color = Color.ORANGE;
    }

    @Override
    public void selfMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        Player player = gameController.getPlayer();
        BoardElement[][] boardElements = gameController.getBoardArrayObject();

        String shortestPath = ShortestPath.getShortestMazePathStart(getXPosition(), getYPosition(), player.getXPosition(),
                player.getYPosition(), "", 's', boardElements, boardElements.length);

        if (!shortestPath.isEmpty()) {
            moveTo(shortestPath.charAt(0));
        } else {
            String path = ShortestPath.getShortestPathStart(getXPosition(), getYPosition(), player.getXPosition(),
                    player.getYPosition(), "", 's', boardElements, boardElements.length);

            if (path.charAt(0) == 'u' && gameController.isFrozenAtLoc(0, -1, this)) {
                gameController.beatIce(getXPosition(), getYPosition() - 1);
                return;
            } else if (path.charAt(0) == 'r' && gameController.isFrozenAtLoc(1, 0, this)) {
                gameController.beatIce(getXPosition() + 1, getYPosition());
                return;
            } else if (path.charAt(0) == 'd' && gameController.isFrozenAtLoc(0, 1, this)) {
                gameController.beatIce(getXPosition(), getYPosition() + 1);
                return;
            } else if (path.charAt(0) == 'l' && gameController.isFrozenAtLoc(-1, 0, this)) {
                gameController.beatIce(getXPosition() - 1, getYPosition());
                return;
            }

            moveTo(path.charAt(0));
        }
    }
}
