package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Rotation;
import Logic.GameController;

import java.awt.*;

/**
 * Abstract representation of a monster in the game.
 */
public abstract class Monster extends BoardElement implements moving {
    public static final int MAX_TRIALS = 3;

    protected Color color;

    protected Monster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
    }

    @Override
    public void selfMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        this.getRotationState().move(canUp, canRight, canDown, canLeft, 0);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(color);
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }

    public Color getColor() {
        return this.color;
    }
}