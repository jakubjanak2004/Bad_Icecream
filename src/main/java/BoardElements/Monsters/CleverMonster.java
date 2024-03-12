package BoardElements.Monsters;

import java.awt.*;

public class CleverMonster extends Monster{
    public CleverMonster(int xPosition, int yPosition, int rot){
        super(xPosition, yPosition, rot);
        super.monsterType = Type.CLEVER;
        super.color = Color.RED;
    }
}
