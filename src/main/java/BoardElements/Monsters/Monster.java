package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Rotation;
import Logic.GameController;

import java.awt.*;

/**
 * Abstract representation of a monster in the game.
 */
public abstract class Monster extends BoardElement implements SelfMovable {
    public static final int MAX_TRIALS = 3;

    protected Color color;

    protected Monster(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
    }

    @Override
    public void selfMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController) {
        move(canUp, canRight, canDown, canLeft, 0);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(getColor());
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }

    protected void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int numberOfTries) {
        if (numberOfTries > Monster.MAX_TRIALS) return;
        if (rot == Rotation.UP) {
            if (canUp) {
                super.yPosition -= 1;
            } else {
                super.rot = Rotation.RIGHT;
                move(false, canRight, canDown, canLeft, (numberOfTries + 1));
            }
        } else if (rot == Rotation.RIGHT) {
            if (canRight) {
                super.xPosition += 1;
            } else {
                super.rot = Rotation.DOWN;
                move(canUp, false, canDown, canLeft, (numberOfTries + 1));
            }
        } else if (rot == Rotation.DOWN) {
            if (canDown) {
                super.yPosition += 1;
            } else {
                super.rot = Rotation.LEFT;
                move(canUp, canRight, false, canLeft, (numberOfTries + 1));
            }
        } else if (rot == Rotation.LEFT) {
            if (canLeft) {
                super.xPosition -= 1;
            } else {
                super.rot = Rotation.UP;
                move(canUp, canRight, canDown, false, (numberOfTries + 1));
            }
        }
    }

    protected void moveTo(char positionChar) {
        switch (positionChar) {
            case 'u':
                this.rot = Rotation.UP;
                break;
            case 'r':
                this.rot = Rotation.RIGHT;
                break;
            case 'd':
                this.rot = Rotation.DOWN;
                break;
            case 'l':
                this.rot = Rotation.LEFT;
                break;
        }
        move(true, true, true, true, 0);
    }

    public Color getColor() {
        return this.color;
    }
}