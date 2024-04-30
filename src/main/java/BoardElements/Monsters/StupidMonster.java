package BoardElements.Monsters;

import java.awt.*;

/**
 * Stupid monster is a representation of a Monster that is just circling around and is not using any smart way to find the player.
 * But is still as deadly as other monsters.
 */
public class StupidMonster extends Monster{
    public StupidMonster(int xPosition, int yPosition, int rot){
        super(xPosition, yPosition, rot);
        super.color = Color.GREEN;
    }
}
