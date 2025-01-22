package BoardElements;

import BoardElements.rotationState.RotationState;

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
     *
     * @param xPosition x movement delta
     */
    public void moveOnx(int xPosition) {
        super.xPosition += xPosition;
    }

    /**
     * Will move the player on y-axis
     *
     * @param yPosition y movement delta
     */
    public void moveOny(int yPosition) {
        super.yPosition += yPosition;
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(getColor());
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
        // painting the rotation
        g.setColor(Color.WHITE);

        super.paint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void rightDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        int firstOffset = (int) (3. / 8. * step);
        int secondOffset = step / 15;
        g.fillOval(getXPosition() * step + widthPadding + step - secondOffset - step / 4, getYPosition() * step + heightPadding + firstOffset, step / 4, step / 4);
    }

    @Override
    public void leftDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        int firstOffset = (int) (3. / 8. * step);
        int secondOffset = step / 15;
        g.fillOval(getXPosition() * step + widthPadding + secondOffset, getYPosition() * step + heightPadding + firstOffset, step / 4, step / 4);
    }

    @Override
    public void downDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        int firstOffset = (int) (3. / 8. * step);
        int secondOffset = step / 15;
        g.fillOval(getXPosition() * step + widthPadding + firstOffset, getYPosition() * step + heightPadding + step - secondOffset - step / 4, step / 4, step / 4);
    }

    @Override
    public void upDirectionPaint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        int firstOffset = (int) (3. / 8. * step);
        int secondOffset = step / 15;
        g.fillOval(getXPosition() * step + widthPadding + firstOffset, getYPosition() * step + heightPadding + secondOffset, step / 4, step / 4);
    }
}