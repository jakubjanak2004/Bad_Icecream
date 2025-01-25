package Model.Reward;

import Model.BoardElement.BoardElement;
import Model.Player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract representation of a class that can be grabbed by the player and have to be collected i order to win a level.
 */
public abstract class Reward extends BoardElement implements Grabbable {
    protected BufferedImage img;
    private boolean taken = false;

    public Reward(int xPosition, int yPosition) {
        super(xPosition, yPosition);

        loadImage();
    }

    @Override
    synchronized public void grab() {
        this.taken = true;
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Fruit Block size: 35 * 31
        int width = step / 2;
        int height = (int) (width / (35.0 / 31.0));

        int x = calcX(step, widthPadding);
        int y = calcY(step, heightPadding);
        int xPaddingInBlock = (step - width) / 2;
        int yPaddingInBlock = (step - height) / 2;

        g.drawImage(img, x + xPaddingInBlock, y + yPaddingInBlock, width, height, null);
    }

    @Override
    public void tryGrabbing(Player player) {
        if (xPosition == player.getXPosition() && yPosition == player.getYPosition()) {
            grab();
        }
    }

    public boolean isTaken() {
        return taken;
    }

    abstract void loadImage();
}
