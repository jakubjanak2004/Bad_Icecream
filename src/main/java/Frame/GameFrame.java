package Frame;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(int dimension) {
        this.setTitle("Bad Ice cream Game");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        GameLabel gameLabel = new GameLabel();
        this.add(gameLabel);

        this.setVisible(true);
    }
}