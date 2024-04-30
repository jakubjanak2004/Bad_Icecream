package BoardElements.Blocks;

import BoardElements.BoardElement;

/**
 * Abstract class representing a block on the GameBoard.
 */
abstract class Block extends BoardElement {
    public Block(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }
}
