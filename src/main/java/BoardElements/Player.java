package BoardElements;

import java.awt.*;

/**
 * Player class is used as an object representation of player playing the game.
 */
public class Player extends BoardElement {
    public Player(int xPosition, int yPosition, Rotation rot) {
        super(xPosition, yPosition, rot);
        super.color = Color.BLACK;
    }

    /**
     * Will move the player on x-axis
     * @param xPosition x movement delta
     */
    public void moveOnx(int xPosition) {
        super.xPosition += xPosition;
    }

    /**
     * Will move the player on y-axis
     * @param yPosition y movement delta
     */
    public void moveOny(int yPosition) {
        super.yPosition += yPosition;
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(getColor());
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }
}