package View;

import BoardElements.Blocks.IceBlock;
import main.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * WelcomeView serves as a window that is being shown when the game is run at first.
 * After this window is closed it can be opened only by turning the game off and on.
 */
public class WelcomeView extends JFrame {
    private BufferedImage img;

    private int moduloCounter = 0;

    private Timer refreshTimer;
    private TimerTask refreshTimerTask;

    // singleton variables
    private static boolean instanceExists = false;
    private static WelcomeView instance;

    /**
     * The Singleton implementation method, if the single instance does not exist it will be created by this method and returned back.
     * If the singleton implementation does exist it will be returned without creating new instance.
     * @param dimension -> The Constructor needs the dimension parameter
     * @return single possible instance of the Welcome View
     */
    public synchronized static  WelcomeView getInstance(int dimension) {
        if (!instanceExists) {
            WelcomeView.instance = new WelcomeView(dimension);
            WelcomeView.instanceExists = true;
        }
        return WelcomeView.instance;
    }

    private WelcomeView(int dimension) {
        this.setTitle("Bad Ice cream Welcome Page");
        this.setSize(dimension, dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        loadImage();

        this.refreshTimer = new Timer();
        this.refreshTimerTask = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        refreshTimer.schedule(refreshTimerTask, 0, 10);

        JPanel welcomeGraphics = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(3, 37, 78));
                g.fillRect(0, 0, getWidth(), getHeight());

                paintSnowflake(g);

                drawHeadline(g);
            }
        };

        this.setContentPane(welcomeGraphics);
        welcomeGraphics.setLayout(null);

        JButton btnNewButton = new JButton("Start Game");
        btnNewButton.setBounds((dimension-250)/2, (dimension-250)/2, 250, 50);
        welcomeGraphics.add(btnNewButton);
        btnNewButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.gameStarted();
                Main.closeWelcomePage();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.setVisible(true);
    }

    private void paintSnowflake(Graphics g){
        int snowFlakesOnScreen = 5;

        for (int i = 0; i < snowFlakesOnScreen * 2; i++) {
            for (int j = 0; j < snowFlakesOnScreen * 2; j++) {
                int step = this.getWidth() / snowFlakesOnScreen;
                g.drawImage(img, step * i + moduloCounter - (step * snowFlakesOnScreen),
                        step * j + moduloCounter - (step * snowFlakesOnScreen), 100, 100, null);
            }
        }

        moduloCounter += 1;
        if (moduloCounter > this.getWidth()) moduloCounter = 0;
    }

    private void drawHeadline(Graphics g){
        int startY = 150;

        String gameOverString = "BAD ICECREAM";

        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 6F);
        g.setFont(newFont);

        int stringLength = (int) g.getFontMetrics().getStringBounds(gameOverString, g).getWidth();
        int stringHeight = (int) g.getFontMetrics().getStringBounds(gameOverString, g).getHeight();
        int startX = (getWidth() - stringLength) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(startX, startY - stringHeight + 15, stringLength, stringHeight);
        g.setColor(Color.WHITE);
        g.drawString(gameOverString, startX, startY);
    }

    public void loadImage() {
        try (InputStream inputStream = IceBlock.class.getClassLoader().getResourceAsStream("assets/snowflake_3.png")) {
            if (inputStream != null) {
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
