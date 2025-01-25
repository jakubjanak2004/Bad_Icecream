package Model.Reward;

import Model.Blocks.IceBlock;
import Model.GameBoard.GameBoard;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * Key is an item that has to be collected in order to open chests.
 */
public class Key extends Reward {

    public Key(int xPosition, int yPosition, GameBoard gameBoard) {
        super(xPosition, yPosition, gameBoard);
    }

    @Override
    protected void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/Key.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
