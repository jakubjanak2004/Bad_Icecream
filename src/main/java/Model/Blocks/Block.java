package Model.Blocks;

import Model.BoardElement.BoardElement;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract class representing a block on the GameBoard.
 */
public abstract class Block extends BoardElement {
    public Block(int xPosition, int yPosition) {
        super(xPosition, yPosition);
        loadImage();
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        int x = calcX(step, widthPadding);
        int y = calcY(step, heightPadding);

        int width = (int) calcWidth(x, y, step);
        int height = (int) calcHeight(x, y, step);
        x = (int) calcXPos(x, y, step);
        y = (int) calcYPos(x, y, step);

        g.drawImage(getImage(), x, y, width, height, null);
    }

    @Override
    public boolean isVisitable() {
        return false;
    }

    protected double calcWidth(int x, int y, int step) {
        return step;
    }
    protected double calcHeight(int x, int y, int step) {
        return step;
    }
    protected double calcXPos(int x, int y, int step) {
        return x;
    }
    protected double calcYPos(int x, int y, int step) {
        return y;
    }

    abstract protected BufferedImage getImage();
    abstract protected void loadImage();
}
