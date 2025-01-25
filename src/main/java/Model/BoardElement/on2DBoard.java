package Model.BoardElement;

/**
 * represents an element that is situated on a 2-dimensional board.
 */
public interface on2DBoard {
    /**
     * The x position on GameBoard array
     * @return the integer position
     */
    int getXPosition();

    /**
     * The y position on GameBoard array
     * @return the integer position
     */
    int getYPosition();
}
