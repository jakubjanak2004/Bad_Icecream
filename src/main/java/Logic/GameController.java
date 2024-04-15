package Logic;

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
import View.GameView;
import LevelManagement.LevelManager;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;
import java.util.logging.Logger;

public class GameController {

    // logging, does not work now
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    // final fields
    private final GameView GAME_VIEW;
    private final Player PLAYER;
    private final LevelManager LEVEL_MANAGER = new LevelManager();
    private final IceManipulator ICE_MANIPULATOR = new IceManipulator(this);
    private final ArrayList<Monster> MONSTERS = new ArrayList<>();
    private final ArrayList<Fruit> FRUIT = new ArrayList<>();
    private final int GAME_LOOP_REFRESH = 50;

    // boolean fields
    private boolean isGameOn = false;
    private boolean isMenuOpened = true;
    private boolean wasLevelWon = false;

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

    // Constructor
    public GameController() {
        this.boardArrayObject = new BoardElement[numOfFields][numOfFields];

        this.PLAYER = new Player(0, 0, 0);

        GAME_VIEW = new GameView(this);

        setGameLoopTimer();
    }

    // timer related methods
    public void setGameLoopTimer() {
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
    public void userClickHandler(KeyEvent e) {

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

    private void checkAllFruitGone() {
        boolean isMoreFruit = false;
        for (Fruit f : FRUIT) {
            if (f.isTaken()) continue;
            isMoreFruit = true;
        }
        if (!isMoreFruit) {
            wasLevelWon = true;
            gameOver();
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

    public boolean isMenuOpened() {
        return isMenuOpened;
    }

    public boolean isWasLevelWon() {
        return wasLevelWon;
    }

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
}