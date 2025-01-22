package BoardElements.Reward;

import BoardElements.Blocks.IceBlock;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * Representation of a fruit that the player must collect.
 */
public class Fruit extends Reward {

    public Fruit(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    protected void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Orange.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
