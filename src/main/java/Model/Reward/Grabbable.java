package Model.Reward;

import Model.Player.Player;

/**
 * Represents a object that can be grabbed.
 */
public interface Grabbable {
    /**
     * Represents a way of saying that you want to grab certain object.
     */
    void grab();
    void tryGrabbing(Player player);
}
