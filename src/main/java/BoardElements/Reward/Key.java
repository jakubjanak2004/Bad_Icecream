package BoardElements.Reward;

import BoardElements.Blocks.IceBlock;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Key is an item that has to be collected in order to open chests.
 */
public class Key extends Reward {
    private BufferedImage img;

    public Key(int xPosition, int yPosition) {
        super(xPosition, yPosition);

        loadImage();
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Key Block size: 35 * 31
        int width = step / 2;
        int height = (int) (width / (35.0 / 31.0));
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;
        int xPaddingInBlock = (step - width) / 2;
        int yPaddingInBlock = (step - height) / 2;

        g.drawImage(img, x + xPaddingInBlock, y + yPaddingInBlock, width, height, null);
    }

    private void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Key.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
