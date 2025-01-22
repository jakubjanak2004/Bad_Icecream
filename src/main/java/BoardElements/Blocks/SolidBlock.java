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
    }

    @Override
    protected BufferedImage getImage() {
        return img;
    }

    @Override
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
