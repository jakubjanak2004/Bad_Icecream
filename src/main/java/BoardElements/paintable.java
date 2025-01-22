package BoardElements;

import java.awt.*;

/**
 * Represents a element that can be painted onto a GameBoard
 */
public interface paintable {

    /**
     * Paint method that can be used for painting the object onto the GameBoard
     * @param g Graphics2D Object for painting
     * @param step the step of the game Board, it is the with between the lines on the GameBoard
     * @param widthPadding the Width Padding the GameBoard is moved by from the left side of the window
     * @param heightPadding the Height Padding the GameBoard is moved by from the upper side of the window
     */
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding);
}
