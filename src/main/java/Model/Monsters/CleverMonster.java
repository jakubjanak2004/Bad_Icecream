package Model.Monsters;

import Model.BoardElement.BoardElement;
import Model.GameBoard.GameBoard;
import Model.Player.Player;
import Model.Player.Rotation;
import Controller.GameController;
import Model.ShortestPath.ShortestPath;

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
    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        Player player = gameBoard.getPlayer();
        Optional<BoardElement>[][] boardElements = gameBoard.getBoardElementArray();

        Rotation rot = ShortestPath.getPathStartNoIce(getXPosition(), getYPosition(), player.getXPosition(), player.getYPosition(), boardElements);

        setRot(rot);

        return true;
    }
}
