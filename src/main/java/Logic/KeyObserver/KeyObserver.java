package Logic.KeyObserver;

import Logic.GameController;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeyObserver {
    private final List<KeySubscriber> subscriberList;

    public KeyObserver(GameController controller) {
        subscriberList = new ArrayList<>();
        addSubscriber(controller);
    }

    public void addSubscriber(KeySubscriber subscriber) {
        subscriberList.add(subscriber);
    }

    public void notify(KeyEvent event) {
        int keyCode = event.getKeyCode();
        for (KeySubscriber subscriber : subscriberList) {
            switch (keyCode) {
                case KeyEvent.VK_RIGHT -> subscriber.rightArrowPressed();
                case KeyEvent.VK_LEFT -> subscriber.leftArrowPressed();
                case KeyEvent.VK_UP -> subscriber.upArrowPressed();
                case KeyEvent.VK_DOWN -> subscriber.downArrowPressed();
                case KeyEvent.VK_SPACE -> subscriber.spacePressed();
                case KeyEvent.VK_R -> subscriber.rKeyPressed();
                case KeyEvent.VK_G -> subscriber.gKeyPressed();
            }
        }
    }
}
