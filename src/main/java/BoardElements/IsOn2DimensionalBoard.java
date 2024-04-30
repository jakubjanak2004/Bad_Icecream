package BoardElements;

/**
 * represents an element that is situated on a 2-dimensional board.
 */
public interface IsOn2DimensionalBoard {
    /**
     * The x position on GameBoard array
     * @return the integer position
     */
    public int getXPosition();

    /**
     * The y position on GameBoard array
     * @return the integer position
     */
    public int getYPosition();
}
