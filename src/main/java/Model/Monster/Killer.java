package Model.Monster;

import Model.Player.Player;

public interface Killer {
    boolean tryKilling(Player player);
}
