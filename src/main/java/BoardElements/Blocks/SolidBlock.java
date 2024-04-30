package BoardElements.Blocks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * SolidBlock class representing the Block that is solid and cannot be broken on the map.
 */
public class SolidBlock extends Block {
    public SolidBlock(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Metal Tile size: 36 * 36
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/Assets/MetalTile.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g.drawImage(img, x, y, step, step, null);
    }
}
