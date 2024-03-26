package Frame;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Fruit.Chest;
import BoardElements.Fruit.Fruit;
import BoardElements.Fruit.Key;
import BoardElements.Monsters.CleverMonster;
import BoardElements.Monsters.Monster;
import BoardElements.Monsters.StrongMonster;
import BoardElements.Monsters.StupidMonster;
import BoardElements.Player;
import LevelManagement.LevelManager;
import Logic.IceManipulator;
import Logic.ShortestPath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameLabel extends JPanel {

    // final fields
    private final Player PLAYER;
    private final LevelManager LEVEL_MANAGER = new LevelManager();
    private final IceManipulator ICE_MANIPULATOR = new IceManipulator(this);
    private final ArrayList<Monster> MONSTERS = new ArrayList<>();
    private final ArrayList<Fruit> FRUIT = new ArrayList<>();
    private final int REFRESH_IN_MILLISECONDS = 100;

    // boolean fields
    private boolean isGameOn = false;
    private boolean isMenuOpened = true;
    private boolean wasLevelWon = false;

    // Integer fields
    private int numOfFields;
    private int levelNum = 0;
    private int mousePressedX = -1;
    private int mousePressedY = -1;

    // Timer related fields
    private Timer monsterTimer;
    private TimerTask monsterTimerTask;
    private Timer refreshTimer;
    private TimerTask refreshTimerTask;

    // board array holding the blocks of the game board
    private BoardElement[][] boardArrayObject;

    // Constructor
    public GameLabel() {
        this.boardArrayObject = new BoardElement[numOfFields][numOfFields];

        this.PLAYER = new Player(0, 0, 0);

        this.setFocusable(true);

        gameRefreshTimerSetMethod();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                userClickHandler(e);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userPressedHandler(e);
            }
        });
    }

    // Getters and Setters
    public ArrayList<Monster> getMONSTERS() {
        return MONSTERS;
    }

    public ArrayList<Fruit> getFRUIT() {
        return FRUIT;
    }

    public Player getPLAYER() {
        return PLAYER;
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public BoardElement[][] getBoardArrayObject() {
        return boardArrayObject;
    }

    // timer related methods
    private void gameRefreshTimerSetMethod() {
        this.refreshTimer = new Timer();
        this.refreshTimerTask = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        refreshTimer.schedule(refreshTimerTask, 0, REFRESH_IN_MILLISECONDS);
    }


    // event handler methods
    private void userPressedHandler(MouseEvent e) {
        if (!isMenuOpened) return;
        mousePressedX = e.getX();
        mousePressedY = e.getY();
    }

    private void userClickHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && isGameOn) {
            PLAYER.setRot(1);
            if (PLAYER.getXPosition() >= numOfFields - 1) {
                return;
            }
            if (!isVisitable(PLAYER.getXPosition() + 1, PLAYER.getYPosition())) {
                return;
            }
            PLAYER.moveOnx(1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && isGameOn) {
            PLAYER.setRot(3);
            if (PLAYER.getXPosition() <= 0) {
                return;
            }
            if (!isVisitable(PLAYER.getXPosition() - 1, PLAYER.getYPosition())) {
                return;
            }
            PLAYER.moveOnx(-1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_UP && isGameOn) {
            PLAYER.setRot(0);
            if (PLAYER.getYPosition() <= 0) {
                return;
            }
            if (!isVisitable(PLAYER.getXPosition(), PLAYER.getYPosition() - 1)) {
                return;
            }
            PLAYER.moveOny(-1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && isGameOn) {
            PLAYER.setRot(2);
            if (PLAYER.getYPosition() >= numOfFields - 1) {
                return;
            }
            if (!isVisitable(PLAYER.getXPosition(), PLAYER.getYPosition() + 1)) {
                return;
            }
            PLAYER.moveOny(1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && isGameOn) {
            ICE_MANIPULATOR.manipulateIceAsync();
        } else if (e.getKeyCode() == 82 && !isGameOn) {
            isMenuOpened = true;
        } else if (e.getKeyCode() == 71 && !isGameOn) {
            isGameOn = true;
            isMenuOpened = false;
            startGame();
        }
    }


    // paint methods
    @Override
    protected void paintComponent(Graphics g) {
        if (isGameOn) paintGame((Graphics2D) g);
        else {
            if (isMenuOpened) {
                menuPage((Graphics2D) g);
            } else {
                stoppedGamePage((Graphics2D) g);
            }
        }
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

        step = boardDimension / this.numOfFields;
        overFlow = boardDimension % this.numOfFields;

        paintGameBoard(g2, widthPadding, step, heightPadding, boardDimension, overFlow);

        paintFruit(g2, step, widthPadding, heightPadding);

        PLAYER.paint(g2, step, widthPadding, heightPadding);

        paintMonsters(g2, step, widthPadding, heightPadding);

        paintGameBlocks(g2, step, widthPadding, heightPadding);

        checkDeath();

        checkFruitTaken();
    }

    private void paintGameBlocks(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        for (int i = 0; i < boardArrayObject.length; i++) {
            for (int j = 0; j < boardArrayObject[i].length; j++) {
                if (boardArrayObject[i][j] != null) {
                    boardArrayObject[i][j].paint(g2, step, widthPadding, heightPadding);
                }
            }
        }
    }

    private void paintMonsters(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        for (Monster m : MONSTERS) {
            m.paint(g2, step, widthPadding, heightPadding);
        }
    }

    private void paintFruit(Graphics2D g2, int step, int widthPadding, int heightPadding) {
        boolean isMoreFruit = false;
        for (Fruit f : FRUIT) {
            if (f.isTaken()) continue;
            f.paint(g2, step, widthPadding, heightPadding);
            isMoreFruit = true;
        }
        if (!isMoreFruit) {
            wasLevelWon = true;
            gameOver();
        }
    }

    private void paintGameBoard(Graphics2D g2, int widthPadding, int step, int heightPadding, int boardDimension, int overFlow) {
        g2.setColor(Color.BLUE);

        for (int row = 0; row < this.numOfFields + 1; row++) {
            for (int column = 0; column < this.numOfFields + 1; column++) {
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
        int levelsSize = LEVEL_MANAGER.getAllLevels().size();
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
                if (counter >= LEVEL_MANAGER.getAllLevels().size()) break;

                int xPos = column * step + widthPadding;
                int yPos = row * step + heightPadding + space;
                int diameter = step - space;
                int radius = diameter / 2;

                g2d.setColor(Color.WHITE);
                g2d.fillOval(xPos, yPos, diameter, diameter);

                if (LEVEL_MANAGER.getScoreOfLevel(counter)) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.BLACK);
                }

                int strLength = (int) g2d.getFontMetrics().getStringBounds((counter + 1) + "", g2d).getWidth();
                int strHeight = (int) g2d.getFontMetrics().getStringBounds((counter + 1) + "", g2d).getHeight();
                g2d.drawString((counter + 1) + "", column * step + widthPadding + step / 2 - (strLength / 2), row * step + heightPadding + space + step / 2 + (strHeight / 4));

                if (Math.sqrt(Math.pow(mousePressedX - (xPos + radius), 2) + Math.pow(mousePressedY - (yPos + radius), 2)) <= radius && (mousePressedX != -1 && mousePressedY != -1)) {
                    isGameOn = true;
                    isMenuOpened = false;
                    mousePressedX = -1;
                    mousePressedY = -1;
                    levelNum = counter;
                    LEVEL_MANAGER.setScoreOfLevel(counter, false);
                    startGame();
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
        if (wasLevelWon) {
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


    // game logic
    private void checkDeath() {
        for (Monster m : MONSTERS) {
            if (Math.abs(m.getXPosition() - PLAYER.getXPosition()) <= 1 && m.getYPosition() == PLAYER.getYPosition()) {
                gameOver();
            } else if (Math.abs(m.getYPosition() - PLAYER.getYPosition()) <= 1 && m.getXPosition() == PLAYER.getXPosition()) {
                gameOver();
            }
        }
    }

    private void checkFruitTaken() {
        for (Fruit f : FRUIT) {
            if (f.getXPosition() == PLAYER.getXPosition() && f.getYPosition() == PLAYER.getYPosition()) {
                boolean canBeOpened = true;
                if (f.getClass() == Chest.class) {
                    for (Fruit key : FRUIT) {
                        if (key.getClass() == Key.class) {
                            if (!key.isTaken()) {
                                canBeOpened = false;
                            }
                        }
                    }
                }
                if (canBeOpened) {
                    f.setTaken(true);
                }

            }
        }
    }

    private void startGame() {

        FRUIT.clear();
        MONSTERS.clear();
        wasLevelWon = false;
        mousePressedX = -1;
        mousePressedY = -1;

        int[][] gameBoard = LEVEL_MANAGER.getAllLevels().get(levelNum).getGAME_BOARD();

        numOfFields = gameBoard.length;
        boardArrayObject = new BoardElement[gameBoard.length][gameBoard[0].length];

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == -1) {
                    PLAYER.setXPosition(i);
                    PLAYER.setYPosition(j);
                } else if (gameBoard[i][j] == 3) {
                    MONSTERS.add(new StupidMonster(i, j, 0));
                } else if (gameBoard[i][j] == 4) {
                    MONSTERS.add(new CleverMonster(i, j, 0));
                } else if (gameBoard[i][j] == 5) {
                    MONSTERS.add(new StrongMonster(i, j, 0));
                } else if (gameBoard[i][j] == 6) {
                    FRUIT.add(new Fruit(i, j));
                } else if (gameBoard[i][j] == 7) {
                    FRUIT.add(new Chest(i, j));
                } else if (gameBoard[i][j] == 8) {
                    FRUIT.add(new Key(i, j));
                } else if (gameBoard[i][j] == 1) {
                    boardArrayObject[i][j] = new IceBlock(i, j);
                } else if (gameBoard[i][j] == 2) {
                    boardArrayObject[i][j] = new SolidBlock(i, j);
                }
            }
        }

        this.monsterTimer = new Timer();
        this.monsterTimerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < MONSTERS.size(); i++) {
                    Monster monster = MONSTERS.get(i);

                    boolean canUp = monster.getYPosition() > 0 && isVisitable(monster.getXPosition(), monster.getYPosition() - 1);
                    boolean canRight = monster.getXPosition() < numOfFields - 1 && isVisitable(monster.getXPosition() + 1, monster.getYPosition());
                    boolean canDown = monster.getYPosition() < numOfFields - 1 && isVisitable(monster.getXPosition(), monster.getYPosition() + 1);
                    boolean canLeft = monster.getXPosition() > 0 && isVisitable(monster.getXPosition() - 1, monster.getYPosition());

                    for (int j = 0; j < i; j++) {
                        Monster loopMonster = MONSTERS.get(j);
                        if (loopMonster.getYPosition() == monster.getYPosition() + 1 && loopMonster.getXPosition() == monster.getXPosition()) {
                            canDown = false;
                        } else if (loopMonster.getYPosition() == monster.getYPosition() - 1 && loopMonster.getXPosition() == monster.getXPosition()) {
                            canUp = false;
                        } else if (loopMonster.getYPosition() == monster.getYPosition() && loopMonster.getXPosition() == monster.getXPosition() - 1) {
                            canLeft = false;
                        } else if (loopMonster.getYPosition() == monster.getYPosition() && loopMonster.getXPosition() == monster.getXPosition() + 1) {
                            canRight = false;
                        }
                    }

                    if (monster.getMonsterType() == Monster.Type.STUPID) {
                        monster.move(canUp, canRight, canDown, canLeft, 0);
                    } else if (monster.getMonsterType() == Monster.Type.CLEVER) {

                        String shortestPath = ShortestPath.getShortestMazePathStart(monster.getXPosition(), monster.getYPosition(), PLAYER.getXPosition(),
                                PLAYER.getYPosition(), "", 's', boardArrayObject, numOfFields);

                        if (!shortestPath.isEmpty()) {
                            monster.moveTo(shortestPath.charAt(0));
                        } else {
                            monster.move(canUp, canRight, canDown, canLeft, 0);
                        }

                    } else if (monster.getMonsterType() == Monster.Type.STRONG) {
                        String shortestPath = ShortestPath.getShortestMazePathStart(monster.getXPosition(), monster.getYPosition(), PLAYER.getXPosition(),
                                PLAYER.getYPosition(), "", 's', boardArrayObject, numOfFields);

                        if (!shortestPath.isEmpty()) {
                            monster.moveTo(shortestPath.charAt(0));
                        } else {
                            String path = ShortestPath.getShortestPathStart(monster.getXPosition(), monster.getYPosition(), PLAYER.getXPosition(),
                                    PLAYER.getYPosition(), "", 's', boardArrayObject, numOfFields);

                            if (path.charAt(0) == 'u' && isFrozenAtLoc(0, -1, monster)) {
                                beatIce(monster.getXPosition(), monster.getYPosition() - 1);
                                continue;
                            } else if (path.charAt(0) == 'r' && isFrozenAtLoc(1, 0, monster)) {
                                beatIce(monster.getXPosition() + 1, monster.getYPosition());
                                continue;
                            } else if (path.charAt(0) == 'd' && isFrozenAtLoc(0, 1, monster)) {
                                beatIce(monster.getXPosition(), monster.getYPosition() + 1);
                                continue;
                            } else if (path.charAt(0) == 'l' && isFrozenAtLoc(-1, 0, monster)) {
                                beatIce(monster.getXPosition() - 1, monster.getYPosition());
                                continue;
                            }

                            monster.moveTo(path.charAt(0));
                        }
                    }
                }
            }
        };
        monsterTimer.schedule(monsterTimerTask, 250, 500);
    }

    private void gameOver() {
        isGameOn = false;
        isMenuOpened = false;
        monsterTimer.cancel();
        monsterTimer.purge();

        if (wasLevelWon) {
            LEVEL_MANAGER.setScoreOfLevel(levelNum, true);
        }

        Graphics2D g2d = (Graphics2D) getGraphics();
        stoppedGamePage(g2d);
    }

    public boolean isVisitable(int x, int y) {
        if (boardArrayObject[x][y] != null) {
            return boardArrayObject[x][y].getClass() != IceBlock.class && boardArrayObject[x][y].getClass() != SolidBlock.class;
        }
        return true;
    }

    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return (boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove] != null
                && boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove].getClass() == IceBlock.class);
    }

    public void beatIce(int x, int y) {
        if (getBoardArrayObject()[x][y] != null && getBoardArrayObject()[x][y].getClass() == IceBlock.class) {
            IceBlock iceBlock = (IceBlock) getBoardArrayObject()[x][y];
            iceBlock.destabilize();

            if (iceBlock.getStability() <= 0) {
                getBoardArrayObject()[x][y] = null;
            }
        }
    }
}