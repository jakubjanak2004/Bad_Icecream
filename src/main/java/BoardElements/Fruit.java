package BoardElements;

import java.awt.*;

public class Fruit extends BoardElement{

    private boolean taken = false;
    private Color color;

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public Color getColor() {
        return color;
    }

    public Fruit(int xPosition, int yPosition){
        super(xPosition, yPosition);
        this.color = Color.DARK_GRAY;
    }
}
