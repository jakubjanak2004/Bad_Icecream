package BoardElements.Monsters;

import BoardElements.BoardElement;
import BoardElements.Player;
import Logic.GameController;

public interface SelfMovable {
    public void selfMove(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, GameController gameController);
    public int getXPosition();
    public int getYPosition();
}
