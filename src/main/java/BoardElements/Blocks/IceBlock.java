package BoardElements.Blocks;

import BoardElements.BoardElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IceBlock extends BoardElement {

    private int stability = 2;

    public IceBlock(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    public int getStability() {
        return stability;
    }

    public void destabilize() {
        if (stability <= 0) return;
        stability--;
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Ice Block size: 36 * 52
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;
        BufferedImage img = null;
        try {
            if (getStability() >= 2){
                img = ImageIO.read(new File("/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/Assets/Ice_Block.png"));
            } else if (getStability() <= 1){
                img = ImageIO.read(new File("/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/Assets/Ice_Block_dest.png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double h = 52.0 * ((double) step / 36.0);
        y -= (int) (h - step);
        g.drawImage(img, x, y, (int) (double) step, (int) h, null);
    }
}
