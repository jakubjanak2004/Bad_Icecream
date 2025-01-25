package Model.Block;

import Model.BoardElement.BoardElement;
import Model.GameBoard.GameBoard;

import javax.imageio.ImageIO;
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

    public IceBlock(int xPosition, int yPosition, GameBoard gameBoard) {
        super(xPosition, yPosition, gameBoard);
    }

    /**
     * Monsters will be using this method to partially break the ice.
     */
    public void destabilize() {
        stability--;
        if (stability <= 0) {
            gameBoard.setBoardElementAt(xPosition, yPosition, new BoardElement(xPosition, yPosition, gameBoard));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IceBlock element) {
            return this.stability == element.stability && super.equals(obj);
        }
        return false;
    }

    @Override
    protected BufferedImage getImage() {
        if (stability >= 2) {
            return imgStable;
        }
        return imgCracked;
    }

    @Override
    protected double calcHeight(int x, int y, int step) {
        return 52.0 * ((double) step / 36.0);
    }

    @Override
    protected double calcYPos(int x, int y, int step) {
        double h = calcHeight(x, y, step);
        y -= (int) (h - step);
        return y;
    }

    @Override
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
}
