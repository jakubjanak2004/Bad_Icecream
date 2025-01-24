package Logic.observer;

import java.awt.event.KeyEvent;

public interface KeySubscriber {
    void rightArrowPressed(KeyEvent event);
    void downArrowPressed(KeyEvent event);
    void leftArrowPressed(KeyEvent event);
    void upArrowPressed(KeyEvent event);
    void spacePressed(KeyEvent event);
    void rKeyPressed(KeyEvent event);
    void gKeyPressed(KeyEvent event);
}
