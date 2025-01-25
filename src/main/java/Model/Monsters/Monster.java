package Model.Monsters;

import Model.BoardElement.BoardElement;
import Model.GameBoard.GameBoard;
import Model.Player.Rotation;

import java.awt.*;

/**
 * Abstract representation of a monster in the game.
 */
public abstract class Monster extends BoardElement implements movable {
    public static final int MAX_TRIALS = 3;

    protected Color color;

    protected Monster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
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

    public Color getColor() {
        return this.color;
    }

    protected boolean shouldMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameBoard gameBoard) {
        return true;
    }
}