package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;

import java.awt.*;

public class StupidMonster extends Monster{
    public StupidMonster(int xPosition, int yPosition, int rot){
        super(xPosition, yPosition, rot);
        super.color = Color.GREEN;
    }
}
