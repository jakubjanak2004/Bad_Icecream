package View;

import Logic.GameController;

import javax.swing.*;

/**
 * GameFrame is a class extending the JFrame java swing object. It is a window on which the game runs.
 */
public class GameFrame extends JFrame {

    // singleton variables
    private static boolean instanceExists = false;
    private static GameFrame instance;

    /**
     * The Singleton implementation method, if the single instance does not exist it will be created by this method and returned back.
     * If the singleton implementation does exist it will be returned without creating new instance.
     * @param dimension -> The Constructor needs the dimension parameter
     * @return single possible instance of the Welcome View
     */
    public static GameFrame getInstance(int dimension) {
        if (!instanceExists) {
            GameFrame.instance = new GameFrame(dimension);
            GameFrame.instanceExists = true;
        }
        return GameFrame.instance;
    }

    /**
     * The Singleton implementation method, if the single instance does not exist it will be created by this method and returned back.
     * If the singleton implementation does exist it will be returned without creating new instance.
     * This is overloaded method that ensures that the second constructor can be called.
     * @param dimension -> The Constructor needs the dimension parameter
     * @param gameController -> the gameController instance passed to the Constructor
     * @param visibility -> Visibility boolean passed to the Constructor
     * @return single possible instance of the Welcome View
     */
    public static GameFrame getInstance(int dimension, GameController gameController, boolean visibility) {
        if (!instanceExists) {
            GameFrame.instance = new GameFrame(dimension, gameController, visibility);
            GameFrame.instanceExists = true;
        }
        return GameFrame.instance;
    }

    private GameFrame(int dimension) {
        this.setTitle("Bad Ice cream Game");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        GameController gameController = new GameController();
        this.add(gameController.getGameView());

        this.setVisible(true);
    }

    private GameFrame(int dimension, GameController gameController, boolean visibility) {
        this.setTitle("Bad Ice cream Game");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(gameController.getGameView());

        this.setVisible(visibility);
    }


}