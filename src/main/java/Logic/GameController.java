package Logic;

import BoardElements.GameBoard.GameBoard;
import BoardElements.GameBoard.GameBoardBuilder;
import BoardElements.Monsters.moving;
import BoardElements.Player;
import BoardElements.Reward.Reward;
import BoardElements.Rotation;
import LevelManagement.LevelManager;
import Logic.KeyObserver.KeySubscriber;
import View.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * The GameController class manages the game logic and coordinates interactions between various game elements.
 * It controls player movement, monster behavior, game state, and level progression.
 */
public class GameController implements KeySubscriber {
    // Constants
    public static final int MONSTER_MOVE_REFRESH = 500;
    public static final int GAME_LOOP_REFRESH = 50;

    // logging
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    // final fields
    private final GameView gameView;

    // level management classes
    private final LevelManager levelManager;
    //Threading Classes
    MonsterThread monsterThread;
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
    private GameBoard gameBoard;

    // Constructor
    public GameController() {
        this.levelManager = new LevelManager();
        gameView = GameView.getInstance(this);
        setGameLoopTimer();
    }

    public GameController(LevelManager levelManager) {
        this.levelManager = levelManager;
        gameView = GameView.getInstance(this);
        setGameLoopTimer();
    }

    public boolean canUp(int x, int y) {
        return y > 0 && gameBoard.isVisitable(x, y - 1);
    }

    public boolean canRight(int x, int y) {
        return x < getNumOfFields() - 1 && gameBoard.isVisitable(x + 1, y);
    }

    public boolean canDown(int x, int y) {
        return y < getNumOfFields() - 1 && gameBoard.isVisitable(x, y + 1);
    }

    public boolean canLeft(int x, int y) {
        return x > 0 && gameBoard.isVisitable(x - 1, y);
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
                gameBoard.checkFruitTaken();
                checkAllFruitGone();
            }
        };
        gameLoopTimer.schedule(gameLoopTimerTask, 800, GAME_LOOP_REFRESH);
    }

    @Override
    public void rightArrowPressed() {
        if (!isGameOn) {
            return;
        }
        gameBoard.getPlayer().setRot(Rotation.RIGHT);
        if (gameBoard.getPlayer().getXPosition() >= numOfFields - 1) {
            return;
        }
        if (!gameBoard.isVisitable(gameBoard.getPlayer().getXPosition() + 1, gameBoard.getPlayer().getYPosition())) {
            return;
        }
        gameBoard.getPlayer().moveOnx(1);
        checkDeath();
        gameBoard.checkFruitTaken();
    }

    @Override
    public void downArrowPressed() {
        if (!isGameOn) {
            return;
        }
        gameBoard.getPlayer().setRot(Rotation.DOWN);
        if (gameBoard.getPlayer().getYPosition() >= numOfFields - 1) {
            return;
        }
        if (!gameBoard.isVisitable(gameBoard.getPlayer().getXPosition(), gameBoard.getPlayer().getYPosition() + 1)) {
            return;
        }
        gameBoard.getPlayer().moveOny(1);
        checkDeath();
        gameBoard.checkFruitTaken();
    }

    @Override
    public void leftArrowPressed() {
        if (!isGameOn) {
            return;
        }
        gameBoard.getPlayer().setRot(Rotation.LEFT);
        if (gameBoard.getPlayer().getXPosition() <= 0) {
            return;
        }
        if (!gameBoard.isVisitable(gameBoard.getPlayer().getXPosition() - 1, gameBoard.getPlayer().getYPosition())) {
            return;
        }
        gameBoard.getPlayer().moveOnx(-1);
        checkDeath();
        gameBoard.checkFruitTaken();
    }

    @Override
    public void upArrowPressed() {
        if (!isGameOn) {
            return;
        }
        gameBoard.getPlayer().setRot(Rotation.UP);
        if (gameBoard.getPlayer().getYPosition() <= 0) {
            return;
        }
        if (!gameBoard.isVisitable(gameBoard.getPlayer().getXPosition(), gameBoard.getPlayer().getYPosition() - 1)) {
            return;
        }
        gameBoard.getPlayer().moveOny(-1);
        checkDeath();
        gameBoard.checkFruitTaken();
    }

    @Override
    public void spacePressed() {
        if (!isGameOn) {
            return;
        }
        gameBoard.getIceManipulator().manipulateIceAsync();
    }

    @Override
    public void rKeyPressed() {
        if (isGameOn) {
            return;
        }
        isMenuOpened = true;
    }

    @Override
    public void gKeyPressed() {
        if (isGameOn) {
            return;
        }
        isGameOn = true;
        isMenuOpened = false;
        startGame();
    }

    private void checkDeath() {
        if (gameBoard.checkDeath()) {
            gameOver();
        }
    }

    private void checkAllFruitGone() {
        if (gameBoard.checkAllFruitGone()) {
            wasLevelWon = true;
            gameOver();
        }
    }

    /**
     * This method is for starting the game.
     * It has to be public for testing purposes.
     */
    public boolean startGame() {
        logger.info("Game was started");

        wasLevelWon = false;

        gameBoard = GameBoardBuilder.builder()
                .setBoardElementArray(levelManager.getAllLevels().get(levelNum).getGAME_BOARDCopy())
                .build();

        numOfFields = gameBoard.getBoardElementArray().length;

        isGameOn = true; // is game on moved so that the view fill fetch the game data later
        checkState = true;

        monsterThread = new MonsterThread(this);

        if (this.monstersMove) {
            monsterThread.start();
        }

        // game was started
        return true;
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

    public boolean isGameOn() {
        return isGameOn;
    }

    public boolean isMenuOpened() {
        return isMenuOpened;
    }

    public boolean isWasLevelWon() {
        return wasLevelWon;
    }

    public List<moving> getMonsters() {
        return gameBoard.getMonsters();
    }

    public List<Reward> getRewards() {
        return new ArrayList<>(gameBoard.getRewards());
    }

    public Player getPlayer() {
        return gameBoard.getPlayer();
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public void setNumOfFields(int numOfFields) {
        this.numOfFields = numOfFields;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public int getMONSTER_MOVE_REFRESH() {
        return MONSTER_MOVE_REFRESH;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public void setMonstersMove(boolean monstersMove) {
        this.monstersMove = monstersMove;
    }
}