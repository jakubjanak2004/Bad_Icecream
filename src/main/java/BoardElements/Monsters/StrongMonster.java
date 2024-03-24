package BoardElements.Monsters;

import java.awt.*;

public class StrongMonster extends Monster{
    public StrongMonster(int xPosition, int yPosition, int rot) {
        super(xPosition, yPosition, rot);
        super.monsterType = Type.STRONG;
        super.color = Color.ORANGE;
    }

    @Override
    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int tries) {
        super.move(canUp, canRight, canDown, canLeft, tries);
    }
}
