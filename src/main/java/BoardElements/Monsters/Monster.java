package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import Logic.GameController;

import java.awt.*;

/**
 * Abstract representation of a monster in the game.
 */
public abstract class Monster extends BoardElement implements SelfMovable {
    protected Color color;

    protected Monster(int xPosition, int yPosition, int rot) {
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

    protected void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int tries) {
        if (tries > 3) return;
        if (rot == 0) {
            if (canUp) {
                super.yPosition -= 1;
            } else {
                super.rot = 1;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 1) {
            if (canRight) {
                super.xPosition += 1;
            } else {
                super.rot = 2;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 2) {
            if (canDown) {
                super.yPosition += 1;
            } else {
                super.rot = 3;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 3) {
            if (canLeft) {
                super.xPosition -= 1;
            } else {
                super.rot = 0;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        }
    }

    protected void moveTo(char positionChar) {
        switch (positionChar) {
            case 'u':
                this.rot = 0;
                break;
            case 'r':
                this.rot = 1;
                break;
            case 'd':
                this.rot = 2;
                break;
            case 'l':
                this.rot = 3;
                break;
        }
        move(true, true, true, true, 0);
    }

    public Color getColor() {
        return this.color;
    }
}