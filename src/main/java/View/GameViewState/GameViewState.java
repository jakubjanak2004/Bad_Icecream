package View.GameViewState;

import View.GameView;

import java.awt.*;

public abstract class GameViewState {
    private final GameView gameView;

    public GameViewState(GameView gameView) {
        this.gameView = gameView;
    }

    abstract public void paintState(Graphics2D g);

    abstract public void startPlaying();
    abstract public void endPlaying();
    abstract public void toMenu();
}
