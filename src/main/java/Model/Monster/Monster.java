package Model.Monster;

import Model.BoardElement.BoardElement;
import Model.GameBoard.GameBoard;
import Model.Player.Player;
import Model.Player.Rotation;

import java.awt.*;

/**
 * Abstract representation of a monster in the game.
 */
public abstract class Monster extends BoardElement implements Movable, Killer {
    public static final int MAX_TRIALS = 3;

    protected Color color;

    protected Monster(int xPosition, int yPosition, Rotation rot, GameBoard gameBoard) {
        super(xPosition, yPosition, rot, gameBoard);
    }

    @Override
    public final void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        if (shouldMove(canUp, canRight, canDown, canLeft, gameBoard)) {
            this.getRotationState().move(canUp, canRight, canDown, canLeft, 0);
        }
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(color);
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }

    @Override
    public boolean tryKilling(Player player) {
        if (Math.abs(getXPosition() - player.getXPosition()) <= 1 && getYPosition() == player.getYPosition()) {
            return true;
        }
        return Math.abs(getYPosition() - player.getYPosition()) <= 1 && getXPosition() == player.getXPosition();
    }

    public Color getColor() {
        return this.color;
    }

    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        return true;
    }
}