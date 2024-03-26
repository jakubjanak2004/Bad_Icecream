package BoardElements.Fruit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Key extends Fruit {
    public Key(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Key Block size: 35 * 31
        int width = step / 2;
        int height = (int) (width / (35.0/31.0));
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;
        int xPaddingInBlock = (step - width) / 2;
        int yPaddingInBlock = (step - height) / 2;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/Assets/key.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g.drawImage(img, x + xPaddingInBlock, y + yPaddingInBlock, width, height, null);
    }
}
