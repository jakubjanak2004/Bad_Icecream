package BoardElements;

import java.awt.*;

public class Player extends BoardElement {
    public void moveOnx(int xPosition) {
        super.xPosition += xPosition;
    }

    public void moveOny(int yPosition) {
        super.yPosition += yPosition;
    }

    public Color getColor() {
        return color;
    }

    public Player(int xPosition, int yPosition, int rot) {
        super(xPosition, yPosition, rot);
        super.color = Color.BLACK;
    }

    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(getColor());
        g.fillOval(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
    }
}