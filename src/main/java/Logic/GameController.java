package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Fruit.Chest;
import BoardElements.Fruit.Fruit;
import BoardElements.Fruit.Key;
import BoardElements.Fruit.Reward;
import BoardElements.Monsters.*;
import BoardElements.Player;
import LevelManagement.LevelManager;
import View.GameView;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class GameController {

    // logging, does not work now
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    // final fields
    private final GameView GAME_VIEW;
    private final Player PLAYER;
    private final LevelManager LEVEL_MANAGER = new LevelManager();
    private final IceManipulator ICE_MANIPULATOR = new IceManipulator(this);
    private final ArrayList<SelfMovable> MONSTERS = new ArrayList<>();
    private final ArrayList<Reward> REWARD = new ArrayList<>();
    private final int GAME_LOOP_REFRESH = 50;
    private final int MONSTER_MOVE_REFRESH = 500;

    // boolean fields
    private boolean isGameOn = false;
    private boolean isMenuOpened = true;
    private boolean wasLevelWon = false;
    private boolean isRefreshing = false;

    // Integer fields
     private int numOfFields;
    private int levelNum = 0;

    // Timer related fields
    private Timer monsterTimer;
    private TimerTask monsterTimerTask;
    private Timer gameLoopTimer;
    private TimerTask gameLoopTimerTask;

    // Objects Array
    private BoardElement[][] boardArrayObject;

    // Reference object
    GameController gameController = this;

    // Constructor
    public GameController() {
        this.boardArrayObject = new BoardElement[numOfFields][numOfFields];

        this.PLAYER = new Player(0, 0, 0);

        GAME_VIEW = new GameView(this);

        setGameLoopTimer();
    }

    // timer related methods
    public void setGameLoopTimer() {

        logger.info("Graphics Refresh loop was started!");

        isRefreshing = true;
        this.gameLoopTimer = new Timer();
        this.gameLoopTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isGameOn) return;

                checkDeath();
                checkFruitTaken();

                checkAllFruitGone();
            }
        };
        gameLoopTimer.schedule(gameLoopTimerTask, 0, GAME_LOOP_REFRESH);
    }

    // handler method
    public void userTypeHandler(KeyEvent e) {

        logger.info("User just pressed a key");

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


    // public methods
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

    public boolean isFrozenAtLoc(int x, int y) {

        if (x < 0 || x >= boardArrayObject.length) {
            return false;
        } else if (y < 0 || y >= boardArrayObject[0].length) {
            return false;
        }

        return (boardArrayObject[x][y] != null
                && boardArrayObject[x][y].getClass() == IceBlock.class);
    }

    public boolean isFrozenAtLoc(int xMove, int yMove, Monster monster) {
        return (boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove] != null
                && boardArrayObject[monster.getXPosition() + xMove][monster.getYPosition() + yMove].getClass() == IceBlock.class);
    }

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

    private void startGame() {

        logger.info("Game was started");

        REWARD.clear();
        MONSTERS.clear();
        wasLevelWon = false;

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
                    REWARD.add(new Fruit(i, j));
                } else if (gameBoard[i][j] == 7) {
                    REWARD.add(new Chest(i, j));
                } else if (gameBoard[i][j] == 8) {
                    REWARD.add(new Key(i, j));
                } else if (gameBoard[i][j] == 1) {
                    boardArrayObject[i][j] = new IceBlock(i, j);
                } else if (gameBoard[i][j] == 2) {
                    boardArrayObject[i][j] = new SolidBlock(i, j);
                } else if (gameBoard[i][j] == 0){
                    boardArrayObject[i][j] = new BoardElement(i, j);
                }
            }
        }

        this.monsterTimer = new Timer();
        this.monsterTimerTask = new TimerTask() {
            @Override
            public void run() {

                for (int i = 0; i < MONSTERS.size(); i++) {

                    if (!(MONSTERS.get(i) instanceof Monster)){
                        continue;
                    }

                    Monster monster = (Monster) MONSTERS.get(i);

                    boolean canUp = monster.getYPosition() > 0 && isVisitable(monster.getXPosition(), monster.getYPosition() - 1);
                    boolean canRight = monster.getXPosition() < numOfFields - 1 && isVisitable(monster.getXPosition() + 1, monster.getYPosition());
                    boolean canDown = monster.getYPosition() < numOfFields - 1 && isVisitable(monster.getXPosition(), monster.getYPosition() + 1);
                    boolean canLeft = monster.getXPosition() > 0 && isVisitable(monster.getXPosition() - 1, monster.getYPosition());

                    for (int j = 0; j < i; j++) {
                        Monster loopMonster = (Monster) MONSTERS.get(j);
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

                    monster.selfMove(canUp, canRight, canDown, canLeft, gameController);
                }
            }
        };
        monsterTimer.schedule(monsterTimerTask, 250, MONSTER_MOVE_REFRESH);
    }

    private void gameOver() {

        logger.info("Game stopped (you either won or got killed by a monster)");

        isGameOn = false;
        isMenuOpened = false;
        if (monsterTimer != null) {
            monsterTimer.cancel();
            monsterTimer.purge();
        }

        if (wasLevelWon) {
            LEVEL_MANAGER.setScoreOfLevel(levelNum, true);
        }
    }


    // Getters and Setters
    public GameView getGAME_VIEW() {
        return GAME_VIEW;
    }

    public void setMenuButtonClickedOn(int counter) {
        isGameOn = true;
        isMenuOpened = false;
        levelNum = counter;
        LEVEL_MANAGER.setScoreOfLevel(counter, false);
        startGame();
    }

    public LevelManager getLEVEL_MANAGER() {
        return LEVEL_MANAGER;
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

    public ArrayList<SelfMovable> getMONSTERS() {
        return MONSTERS;
    }

    public ArrayList<Reward> getREWARD() {
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
}