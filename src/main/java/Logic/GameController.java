package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Monsters.*;
import BoardElements.Player;
import BoardElements.Reward.Chest;
import BoardElements.Reward.Fruit;
import BoardElements.Reward.Key;
import BoardElements.Reward.Reward;
import LevelManagement.LevelManager;
import View.GameView;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.logging.Logger;

/**
 * The GameController class manages the game logic and coordinates interactions between various game elements.
 * It controls player movement, monster behavior, game state, and level progression.
 */
public class GameController {

    // logging
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    // final fields
    private final GameView GAME_VIEW;
    private final Player PLAYER;
    private final IceManipulator ICE_MANIPULATOR = new IceManipulator(this);
    private final List<SelfMovable> MONSTERS = Collections.synchronizedList(new ArrayList<>());
    private final List<Reward> REWARD = Collections.synchronizedList(new ArrayList<>());
    private final int GAME_LOOP_REFRESH = 50;
    private final int MONSTER_MOVE_REFRESH = 500;
    //Threading Classes
    MonsterThread monsterThread;
    // level management classes
    private LevelManager levelManager;
    // boolean fields
    private boolean isGameOn = false;
    private boolean isMenuOpened = true;
    private boolean wasLevelWon = false;
    private boolean isRefreshing = false;
    private boolean checkState = false;
    private boolean monstersMove = true;

    // Integer fields
    private int numOfFields;
    private int levelNum = 0;

    // Timer related fields
    private Timer gameLoopTimer;
    private TimerTask gameLoopTimerTask;
    // Objects Array
    private BoardElement[][] boardArrayObject;

    // Constructor
    public GameController() {
        this.levelManager = new LevelManager();

        this.boardArrayObject = new BoardElement[numOfFields][numOfFields];

        this.PLAYER = new Player(0, 0, 0);

        GAME_VIEW = new GameView(this);

        setGameLoopTimer();
    }

    public GameController(LevelManager levelManager) {
        this.levelManager = levelManager;

        this.boardArrayObject = new BoardElement[numOfFields][numOfFields];

        this.PLAYER = new Player(0, 0, 0);

        GAME_VIEW = new GameView(this);

        setGameLoopTimer();
    }

    /**
     * setGameLoopTimer method
     * is a method starts th gameLoop timer,
     * checking the conditions for death, fruit being taken and level being won
     */
    public void setGameLoopTimer() {

        logger.config("Graphics Refresh loop was started!");

        isRefreshing = true;
        this.gameLoopTimer = new Timer();
        this.gameLoopTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!checkState) return;

                checkDeath();
                checkFruitTaken();
                checkAllFruitGone();
            }
        };
        gameLoopTimer.schedule(gameLoopTimerTask, 800, GAME_LOOP_REFRESH);
    }

    /**
     * @param e KeyEvent received when user types
     */
    public void userTypeHandler(KeyEvent e) {

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


    /**
     * @param x x location on board array
     * @param y y location on board array
     * @return true if is visitable by player or monster
     */
    public boolean isVisitable(int x, int y) {

        if (x < 0 || x >= boardArrayObject.length) {
            return false;
        } else if (y < 0 || y >= boardArrayObject[0].length) {
            return false;
        }

        if (boardArrayObject[x][y] != null) {
            return boardArrayObject[x][y].getClass() != IceBlock.class && boardArrayObject[x][y].getClass() != SolidBlock.class;
        }
        return true;
    }

    /**
     * @param x x position on game board
     * @param y y position on game board
     * @return true if there is ice at certain location
     */
    public boolean isFrozenAtLoc(int x, int y) {

        if (x < 0 || x >= boardArrayObject.length) {
            return false;
        } else if (y < 0 || y >= boardArrayObject[0].length) {
            return false;
        }

        return (boardArrayObject[x][y] != null
                && boardArrayObject[x][y].getClass() == IceBlock.class);
    }

    /**
     * @param xMove   movement on x axis
     * @param yMove   movement on y axis
     * @param monster monster that is performing the movement
     * @return is frozen at the location the monster is moving to
     */
    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return (boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove] != null
                && boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove].getClass() == IceBlock.class);
    }

    /**
     * The method will beat ice at (x, y) coordinate
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void beatIce(int x, int y) {

        if (x < 0 || x >= getBoardArrayObject().length) {
            return;
        } else if (y < 0 || y >= getBoardArrayObject()[0].length) {
            return;
        }

        if (getBoardArrayObject()[x][y] != null && getBoardArrayObject()[x][y].getClass() == IceBlock.class) {
            IceBlock iceBlock = (IceBlock) getBoardArrayObject()[x][y];
            iceBlock.destabilize();

            if (iceBlock.getStability() <= 0) {
                BoardElement replacement = new BoardElement(x, y);
                getBoardArrayObject()[x][y] = replacement;
            }
        }
    }

    /**
     * This method is for starting the game.
     * It has to be public for testing purposes.
     */
    public void startGame() {
        logger.info("Game was started");

        REWARD.clear();
        MONSTERS.clear();
        wasLevelWon = false;
        isGameOn = true;

        int[][] gameBoard = levelManager.getAllLevels().get(levelNum).getGAME_BOARDCopy();

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
                    REWARD.add(new Fruit(i, j));
                } else if (gameBoard[i][j] == 7) {
                    REWARD.add(new Chest(i, j));
                } else if (gameBoard[i][j] == 8) {
                    REWARD.add(new Key(i, j));
                } else if (gameBoard[i][j] == 1) {
                    boardArrayObject[i][j] = new IceBlock(i, j);
                } else if (gameBoard[i][j] == 2) {
                    boardArrayObject[i][j] = new SolidBlock(i, j);
                } else if (gameBoard[i][j] == 0) {
                    boardArrayObject[i][j] = new BoardElement(i, j);
                }
            }
        }
        checkState = true;
        monsterThread = new MonsterThread(this);

        if (this.monstersMove) {
            monsterThread.start();
        }
    }

    // private methods
    private void checkAllFruitGone() {
        boolean isMoreFruit = false;
        for (Reward f : REWARD) {
            if (f.isTaken()) continue;
            isMoreFruit = true;
        }
        if (!isMoreFruit) {
            wasLevelWon = true;
            gameOver();
        }
    }

    private void checkDeath() {
        for (SelfMovable m : MONSTERS) {
            if (Math.abs(m.getXPosition() - PLAYER.getXPosition()) <= 1 && m.getYPosition() == PLAYER.getYPosition()) {
                gameOver();
            } else if (Math.abs(m.getYPosition() - PLAYER.getYPosition()) <= 1 && m.getXPosition() == PLAYER.getXPosition()) {
                gameOver();
            }
        }
    }

    private void checkFruitTaken() {
        for (Reward f : REWARD) {
            if (f.getXPosition() == PLAYER.getXPosition() && f.getYPosition() == PLAYER.getYPosition()) {
                boolean canBeOpened = true;
                if (f instanceof Chest) {
                    for (Reward key : REWARD) {
                        if (key instanceof Key) {
                            if (!key.isTaken()) {
                                canBeOpened = false;
                            }
                        }
                    }
                }
                if (canBeOpened) {
                    f.grab();
                }
            }
        }
    }

    private void gameOver() {
        logger.info("Game stopped (you either won or got killed by a monster)");

        isGameOn = false;
        isMenuOpened = false;
        checkState = false;

        monsterThread.setRunning(false);

        if (wasLevelWon) {
            levelManager.setScoreOfLevel(levelNum, true);
        }
    }

    public GameView getGAME_VIEW() {
        return GAME_VIEW;
    }

    public void setMenuButtonClickedOn(int counter) {
        isMenuOpened = false;
        levelNum = counter;
        levelManager.setScoreOfLevel(counter, false);
        startGame();
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    // Getters and Setters
    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public boolean isGameOn() {
        return isGameOn;
    }

    public void setGameOn(boolean gameOn) {
        isGameOn = gameOn;
    }

    public boolean isMenuOpened() {
        return isMenuOpened;
    }

    public boolean isWasLevelWon() {
        return wasLevelWon;
    }

    public List<SelfMovable> getMONSTERS() {
        return MONSTERS;
    }

    public List<Reward> getREWARD() {
        return REWARD;
    }

    public Player getPLAYER() {
        return PLAYER;
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public void setNumOfFields(int numOfFields) {
        this.numOfFields = numOfFields;
    }

    public BoardElement[][] getBoardArrayObject() {
        return boardArrayObject;
    }

    public void setBoardArrayObject(BoardElement[][] boardArrayObject) {
        this.boardArrayObject = boardArrayObject;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public int getMONSTER_MOVE_REFRESH() {
        return MONSTER_MOVE_REFRESH;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public void setMonstersMove(boolean monstersMove) {
        this.monstersMove = monstersMove;
    }
}