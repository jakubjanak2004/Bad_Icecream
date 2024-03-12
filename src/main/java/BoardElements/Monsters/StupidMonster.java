package BoardElements.Monsters;

import java.awt.*;

public class StupidMonster extends Monster{
    public StupidMonster(int xPosition, int yPosition, int rot){
        super(xPosition, yPosition, rot);
        super.monsterType = Type.STUPID;
        super.color = Color.GREEN;
    }
}
