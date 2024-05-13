package BoardElements.Blocks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * IceBlock represents a frozen block on the GameBoard.
 */
public class IceBlock extends Block {
    private BufferedImage imgStable;
    private BufferedImage imgCracked;

    private int stability = 2;

    public IceBlock(int xPosition, int yPosition) {
        super(xPosition, yPosition);

        loadImage();
    }

    /**
     * Monsters will be using this method to partially break the ice.
     */
    public void destabilize() {
        if (stability <= 0) return;
        stability--;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IceBlock element) {
            return this.stability == element.stability && super.equals(obj);
        }
        return false;
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        // Ice Block size: 36 * 52
        int x = getXPosition() * step + widthPadding;
        int y = getYPosition() * step + heightPadding;
        BufferedImage img;

        if (getStability() >= 2) {
            img = imgStable;
        } else {
            img = imgCracked;
        }

        double h = 52.0 * ((double) step / 36.0);
        y -= (int) (h - step);
        g.drawImage(img, x, y, (int) (double) step, (int) h, null);
    }

    public void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Ice_Block.png")) {
            if (inputStream != null) {
                imgStable = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Ice_Block_dest.png")) {
            if (inputStream != null) {
                imgCracked = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getStability() {
        return stability;
    }
}
