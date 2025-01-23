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
import BoardElements.Rotation;
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
    // Constants
    public static final int MONSTER_MOVE_REFRESH = 500;
    public static final int GAME_LOOP_REFRESH = 50;

    // logging
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    // final fields
    private final GameView gameView;
    private final Player player;
    private final IceManipulator iceManipulator = new IceManipulator(this);
    private final List<moving> monsters = Collections.synchronizedList(new ArrayList<>());
    private final List<Reward> rewards = Collections.synchronizedList(new ArrayList<>());

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
    private Optional<BoardElement>[][] boardArrayObject;

    // Constructor
    public GameController() {
        this.levelManager = new LevelManager();

        this.boardArrayObject = createEmptyArray(numOfFields, numOfFields);

        this.player = new Player(0, 0, Rotation.UP);

        gameView = GameView.getInstance(this);

        setGameLoopTimer();
    }

    public GameController(LevelManager levelManager) {
        this.levelManager = levelManager;

        this.boardArrayObject = createEmptyArray(numOfFields, numOfFields);

        this.player = new Player(0, 0, Rotation.UP);

        gameView = GameView.getInstance(this);

        setGameLoopTimer();
    }

    public boolean canUp(int x, int y) {
        return y > 0 && isVisitable(x, y - 1);
    }
    public boolean canRight(int x, int y) {
        return x < getNumOfFields() - 1 && isVisitable(x + 1, y);
    }
    public boolean canDown(int x, int y) {
        return y < getNumOfFields() - 1 && isVisitable(x, y + 1);
    }
    public boolean canLeft(int x, int y) {
        return x > 0 && isVisitable(x - 1, y);
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
    public boolean userTypeHandler(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && isGameOn) {
            player.setRot(Rotation.RIGHT);
            if (player.getXPosition() >= numOfFields - 1) {
                return true;
            }
            if (!isVisitable(player.getXPosition() + 1, player.getYPosition())) {
                return true;
            }
            player.moveOnx(1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && isGameOn) {
            player.setRot(Rotation.LEFT);
            if (player.getXPosition() <= 0) {
                return true;
            }
            if (!isVisitable(player.getXPosition() - 1, player.getYPosition())) {
                return true;
            }
            player.moveOnx(-1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_UP && isGameOn) {
            player.setRot(Rotation.UP);
            if (player.getYPosition() <= 0) {
                return true;
            }
            if (!isVisitable(player.getXPosition(), player.getYPosition() - 1)) {
                return true;
            }
            player.moveOny(-1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && isGameOn) {
            player.setRot(Rotation.DOWN);
            if (player.getYPosition() >= numOfFields - 1) {
                return true;
            }
            if (!isVisitable(player.getXPosition(), player.getYPosition() + 1)) {
                return true;
            }
            player.moveOny(1);
            checkDeath();
            checkFruitTaken();

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && isGameOn) {
            iceManipulator.manipulateIceAsync();
        } else if (e.getKeyCode() == 82 && !isGameOn) {
            isMenuOpened = true;
        } else if (e.getKeyCode() == 71 && !isGameOn) {
            isGameOn = true;
            isMenuOpened = false;
            return startGame();
        }else{
            // when unrecognised key is pressed
            return false;
        }
        return true;
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

        if (boardArrayObject[x][y].isPresent()) {
            return boardArrayObject[x][y].get().getClass() != IceBlock.class && boardArrayObject[x][y].get().getClass() != SolidBlock.class;
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

        return (boardArrayObject[x][y].isPresent()
                && boardArrayObject[x][y].get().getClass() == IceBlock.class);
    }

    /**
     * @param xMove   movement on x axis
     * @param yMove   movement on y axis
     * @param monster monster that is performing the movement
     * @return is frozen at the location the monster is moving to
     */
    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return (boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove].isPresent()
                && boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove].get().getClass() == IceBlock.class);
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

        if (getBoardArrayObject()[x][y].isPresent() && getBoardArrayObject()[x][y].get().getClass() == IceBlock.class) {
            IceBlock iceBlock = (IceBlock) getBoardArrayObject()[x][y].get();
            iceBlock.destabilize();

            if (iceBlock.getStability() <= 0) {
                BoardElement replacement = new BoardElement(x, y);
                getBoardArrayObject()[x][y] = Optional.of(replacement);
            }
        }
    }

    /**
     * This method is for starting the game.
     * It has to be public for testing purposes.
     */
    public boolean startGame() {
        logger.info("Game was started");

        rewards.clear();
        monsters.clear();
        wasLevelWon = false;

        int[][] gameBoard = levelManager.getAllLevels().get(levelNum).getGAME_BOARDCopy();

        numOfFields = gameBoard.length;

        boardArrayObject = createEmptyArray(gameBoard.length, gameBoard[0].length);

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == -1) {
                    player.setXPosition(i);
                    player.setYPosition(j);
                } else if (gameBoard[i][j] == 3) {
                    monsters.add(new StupidMonster(i, j, Rotation.UP));
                } else if (gameBoard[i][j] == 4) {
                    monsters.add(new CleverMonster(i, j, Rotation.UP));
                } else if (gameBoard[i][j] == 5) {
                    monsters.add(new StrongMonster(i, j, Rotation.UP));
                } else if (gameBoard[i][j] == 6) {
                    rewards.add(new Fruit(i, j));
                } else if (gameBoard[i][j] == 7) {
                    rewards.add(new Chest(i, j));
                } else if (gameBoard[i][j] == 8) {
                    rewards.add(new Key(i, j));
                } else if (gameBoard[i][j] == 1) {
                    boardArrayObject[i][j] = Optional.of(new IceBlock(i, j));
                } else if (gameBoard[i][j] == 2) {
                    boardArrayObject[i][j] = Optional.of(new SolidBlock(i, j));
                } else if (gameBoard[i][j] == 0) {
                    boardArrayObject[i][j] = Optional.of(new BoardElement(i, j));
                }
            }
        }

        isGameOn = true; // is game on moved so that the view fill fetch the game data later
        checkState = true;

        monsterThread = new MonsterThread(this);

        if (this.monstersMove) {
            monsterThread.start();
        }

        // game was started
        return true;
    }

    // private methods
    private void checkAllFruitGone() {
        boolean isMoreFruit = false;
        for (Reward f : rewards) {
            if (f.isTaken()) continue;
            isMoreFruit = true;
        }
        if (!isMoreFruit) {
            wasLevelWon = true;
            gameOver();
        }
    }

    private void checkDeath() {
        for (moving m : monsters) {
            if (Math.abs(m.getXPosition() - player.getXPosition()) <= 1 && m.getYPosition() == player.getYPosition()) {
                gameOver();
            } else if (Math.abs(m.getYPosition() - player.getYPosition()) <= 1 && m.getXPosition() == player.getXPosition()) {
                gameOver();
            }
        }
    }

    private void checkFruitTaken() {
        for (Reward f : rewards) {
            if (f.getXPosition() == player.getXPosition() && f.getYPosition() == player.getYPosition()) {
                boolean canBeOpened = true;
                if (f instanceof Chest) {
                    for (Reward key : rewards) {
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

    public GameView getGameView() {
        return gameView;
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

    private static Optional<BoardElement>[][] createEmptyArray(int rows, int columns) {
        Optional<BoardElement>[][] array = new Optional[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i][j] = Optional.empty();
            }
        }
        return array;
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

    public List<moving> getMonsters() {
        return monsters;
    }

    public List<Reward> getRewards() {
        return new ArrayList<>(rewards);
    }

    public Player getPlayer() {
        return player;
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public void setNumOfFields(int numOfFields) {
        this.numOfFields = numOfFields;
    }

    public Optional<BoardElement>[][] getBoardArrayObject() {
        return boardArrayObject;
    }

    public void setBoardArrayObject(Optional<BoardElement>[][] boardArrayObject) {
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