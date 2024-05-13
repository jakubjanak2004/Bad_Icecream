package BoardElements.Blocks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * SolidBlock class representing the Block that is solid and cannot be broken on the map.
 */
public class SolidBlock extends Block {
    private BufferedImage img;

    public SolidBlock(int xPosition, int yPosition) {
        super(xPosition, yPosition);

        loadImage();
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Metal Tile size: 36 * 36
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;

        g.drawImage(img, x, y, step, step, null);
    }

    public void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/MetalTile.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
