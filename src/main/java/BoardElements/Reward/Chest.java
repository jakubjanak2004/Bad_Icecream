package BoardElements.Reward;

import BoardElements.Player.Player;

import java.util.List;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Representation of a chest containing the fruit.
 */
public class Chest extends Fruit {
    private List<Key> keys;

    public Chest(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    public void paint(Graphics2D g, int step, int widthPadding, int heightPadding) {
        g.setColor(new Color(150, 75, 0));
        g.fillRect(getXPosition() * step + widthPadding, getYPosition() * step + heightPadding, step, step);
        super.paint(g, step, widthPadding, heightPadding);
    }

    @Override
    public void tryGrabbing(Player player) {
        for (Key key : keys) {
            if (!key.isTaken()) {
                return;
            }
        }
        super.tryGrabbing(player);
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }
}
