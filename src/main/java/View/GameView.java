package View;

import BoardElements.Monsters.Monster;
import BoardElements.Monsters.SelfMovable;
import BoardElements.Reward.Reward;
import Logic.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Is a view that shows us the Game and pages relating to it (level win/lost and menu). The View Part of the MVC framework.
 */
public class GameView extends JLabel {
    private static final int REFRESH_IN_MILLISECONDS = 25;

    private int mousePressedX = -1;
    private int mousePressedY = -1;
    private boolean painting = false;
    private GameController gameController;

    // timer related fields
    private Timer refreshTimer;
    private TimerTask refreshTimerTask;

    public GameView(GameController gameController) {

        this.setFocusable(true);

        this.gameController = gameController;

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameController.userTypeHandler(e);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userPressedHandler(e);
            }
        });

        gameRefreshTimerSetMethod();
    }

    // handler related methods
    private void userPressedHandler(MouseEvent e) {
        if (!gameController.isMenuOpened()) return;
        mousePressedX = e.getX();
        mousePressedY = e.getY();
    }

    /**
     * Is used as a method that will be refreshing the game a t a set rate.
     */
    public void gameRefreshTimerSetMethod() {
        this.refreshTimer = new Timer();
        this.refreshTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (painting) return;
                repaint();
            }
        };
        refreshTimer.schedule(refreshTimerTask, 0, REFRESH_IN_MILLISECONDS);
    }

    // paint methods
    @Override
    protected void paintComponent(Graphics g) {
        painting = true;
        if (gameController.isGameOn()) paintGame((Graphics2D) g);
        else {
            if (gameController.isMenuOpened()) {
                menuPage((Graphics2D) g);
            } else {
                stoppedGamePage((Graphics2D) g);
            }
        }
        painting = false;
    }

    private void paintGame(Graphics2D g2) {

        int height = this.getHeight();
        int width = this.getWidth();
        int widthPadding = 0;
        int heightPadding = 0;
        int boardDimension;
        int step;
        int overFlow;

        if (height < width) {
            boardDimension = height;
            widthPadding = (width - height) / 2;
        } else {
            boardDimension = width;
            heightPadding = (height - width) / 2;
        }

        step = boardDimension / gameController.getNumOfFields();
        overFlow = boardDimension % gameController.getNumOfFields();

        paintGameBoard(g2, widthPadding, step, heightPadding, boardDimension, overFlow);

        paintReward(g2, step, widthPadding, heightPadding);

        gameController.getPlayer().paint(g2, step, widthPadding, heightPadding);

        paintMonsters(g2, step, widthPadding, heightPadding);

        paintGameBlocks(g2, step, widthPadding, heightPadding);
    }

    private void paintGameBlocks(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        for (int i = 0; i < gameController.getBoardArrayObject().length; i++) {
            for (int j = 0; j < gameController.getBoardArrayObject()[i].length; j++) {
                if (gameController.getBoardArrayObject()[i][j] != null) {
                    gameController.getBoardArrayObject()[i][j].paint(g2, step, widthPadding, heightPadding);
                }
            }
        }
    }

    private void paintMonsters(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        for (SelfMovable m : gameController.getMonsters()) {
            Monster monster = (Monster) m;
            monster.paint(g2, step, widthPadding, heightPadding);
        }
    }

    private void paintReward(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        for (Reward f : gameController.getRewards()) {
            if (f.isTaken()) continue;
            f.paint(g2, step, widthPadding, heightPadding);
        }
    }

    private void paintGameBoard(Graphics2D g2, int widthPadding, int step, int heightPadding, int boardDimension, int overFlow) {
        g2.setColor(Color.BLUE);

        for (int row = 0; row < gameController.getNumOfFields() + 1; row++) {
            for (int column = 0; column < gameController.getNumOfFields() + 1; column++) {
                g2.drawLine(widthPadding, row * step + heightPadding, boardDimension + widthPadding - overFlow, row * step + heightPadding);
                g2.drawLine(column * step + widthPadding, heightPadding, column * step + widthPadding, boardDimension + heightPadding - overFlow);
            }
        }
    }

    private void drawCentralizedText(Graphics2D g2d, String textString, int textTopPadding) {
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 5.1F);
        g2d.setFont(newFont);

        int stringLen1 = (int) g2d.getFontMetrics().getStringBounds(textString, g2d).getWidth();
        int startX1 = (getWidth() - stringLen1) / 2;

        g2d.drawString(textString, startX1, textTopPadding);
    }

    private void menuPage(Graphics2D g2d) {
        int height = this.getHeight();
        int width = this.getWidth();
        int levelsSize = gameController.getLevelManager().getAllLevels().size();
        int cols = 4;
        int widthPadding;
        int heightPadding;
        int textTopPadding = 100;
        int space = 10;
        int maxSize = 150;
        int boardDimension;
        int step;

        g2d.setColor(Color.BLUE);
        g2d.setColor(new Color(3, 37, 78));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.WHITE);

        String menuString = "menu";
        drawCentralizedText(g2d, "Menu", textTopPadding);

        int stringHeight = (int) g2d.getFontMetrics().getStringBounds(menuString, g2d).getHeight();

        if (height < width) {
            boardDimension = height;

            step = boardDimension / cols;
            if (step > maxSize) step = maxSize;
        } else {
            boardDimension = width;

            step = boardDimension / cols;
            if (step > maxSize) step = maxSize;
        }

        widthPadding = (width - (step * cols)) / 2;
        heightPadding = stringHeight + textTopPadding;

        int counter = 0;
        for (int row = 0; row < levelsSize / cols + 1; row++) {
            for (int column = 0; column < cols; column++) {
                if (counter >= gameController.getLevelManager().getAllLevels().size()) break;

                int xPos = column * step + widthPadding;
                int yPos = row * step + heightPadding + space;
                int diameter = step - space;
                int radius = diameter / 2;

                g2d.setColor(Color.WHITE);
                g2d.fillOval(xPos, yPos, diameter, diameter);

                if (gameController.getLevelManager().getScoreOfLevel(counter)) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.BLACK);
                }

                int strLength = (int) g2d.getFontMetrics().getStringBounds((counter + 1) + "", g2d).getWidth();
                int strHeight = (int) g2d.getFontMetrics().getStringBounds((counter + 1) + "", g2d).getHeight();
                g2d.drawString((counter + 1) + "", column * step + widthPadding + step / 2 - (strLength / 2), row * step + heightPadding + space + step / 2 + (strHeight / 4));

                if (Math.sqrt(Math.pow(mousePressedX - (xPos + radius), 2) + Math.pow(mousePressedY - (yPos + radius), 2)) <= radius && (mousePressedX != -1 && mousePressedY != -1)) {
                    gameController.setMenuButtonClickedOn(counter);
                    mousePressedX = -1;
                    mousePressedY = -1;
                }
                counter++;
            }
        }
    }

    private void stoppedGamePage(Graphics2D g2d) {
        String gameOverString;
        String instructions = "Press r (menu) or g (replay the current level)";

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (gameController.isWasLevelWon()) {
            g2d.setColor(Color.GREEN);
            gameOverString = "LEVEL WON!";
        } else {
            g2d.setColor(Color.RED);
            gameOverString = "GAME OVER!";
        }

        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 5.1F);
        g2d.setFont(newFont);

        int stringLen1 = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getWidth();
        int stringHeight1 = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getHeight();
        int startX1 = (getWidth() - stringLen1) / 2;
        int startY1 = (getHeight() - stringHeight1) / 2;

        g2d.drawString(gameOverString, startX1, startY1);

        currentFont = g2d.getFont();
        newFont = currentFont.deriveFont(currentFont.getSize() * .3F);
        g2d.setFont(newFont);

        int stringHeight2 = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getHeight();
        int startX2 = (getWidth() - stringLen1) / 2;
        int startY2 = getHeight() - stringHeight2;

        g2d.drawString(instructions, startX2, startY2 - 100);
    }
}
