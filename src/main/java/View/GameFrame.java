package View;

import Logic.GameController;

import javax.swing.*;

/**
 * GameFrame is a class extending the JFrame java swing object. It is a window on which the game runs.
 */
public class GameFrame extends JFrame {

    public GameFrame(int dimension) {
        this.setTitle("Bad Ice cream Game");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        GameController gameController = new GameController();
        this.add(gameController.getGameView());

        this.setVisible(true);
    }

    public GameFrame(int dimension, GameController gameController, boolean visibility) {
        this.setTitle("Bad Ice cream Game");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(gameController.getGameView());

        this.setVisible(visibility);
    }
}