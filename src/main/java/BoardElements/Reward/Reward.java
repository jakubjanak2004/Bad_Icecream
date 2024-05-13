package BoardElements.Reward;

import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract representation of a class that can be grabbed by the player and have to be collected i order to win a level.
 */
public abstract class Reward extends BoardElement implements Grabbable {
    private BufferedImage img;
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

        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;
        int xPaddingInBlock = (step - width) / 2;
        int yPaddingInBlock = (step - height) / 2;

        g.drawImage(img, x + xPaddingInBlock, y + yPaddingInBlock, width, height, null);
    }

    public boolean isTaken() {
        return taken;
    }

    private void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Orange.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
