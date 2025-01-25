package Model.Monster;

import Model.GameBoard.GameBoard;
import Model.Player.Player;
import Model.Player.Rotation;
import Model.ShortestPath.ShortestPath;

import java.awt.*;

/**
 * This class represents a monster that is clever and can find the player even if it is hiding.
 */
public class CleverMonster extends Monster {
    public CleverMonster(int xPosition, int yPosition, Rotation rot, GameBoard gameBoard) {
        super(xPosition, yPosition, rot, gameBoard);
        super.color = Color.RED;
    }

    @Override
    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        Player player = gameBoard.getPlayer();
        Rotation rot = ShortestPath.getPathStartNoIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), gameBoard);
        setRot(rot);
        return true;
    }
}
