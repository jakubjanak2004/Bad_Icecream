import BoardElements.Player.Player;
import LevelManagement.Level;
import LevelManagement.LevelManager;
import Logic.GameController;
import View.GameFrame;
import View.GameView;
import org.junit.jupiter.api.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessTests {

    // set the pause between the steps as well as if the view will be Visible
    int pause = 0;
    boolean showTest = false;

    GameView gameView;
    GameFrame gameFrame;

    GameController gameController;
    LevelManager levelManager;
    Level level;
    int[][] gameBoard = {
            {0, 0, 0, 6, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, -1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 1, 1},
            {1, 0, 0, 3, 0, 0, 1}
    };

    KeyEvent event;

    @BeforeAll
    public void setUpTheProcessTest() {
        // mocking the level management backend
        // we will have our own testing level
        levelManager = mock(LevelManager.class);
        level = mock(Level.class);
        ArrayList<Level> allLevels = new ArrayList<>();
        allLevels.add(level);

        when(levelManager.getAllLevels()).thenReturn(allLevels);
        when(level.getGAME_BOARDCopy()).thenReturn(gameBoard);
    }

    @BeforeEach
    public void setUp() {
        gameController = new GameController(levelManager);
        gameController.setLevelNum(0);
        gameController.setMonstersMove(false);

        gameView = GameView.getInstance(gameController);
        gameView.setGameController(gameController);

        gameFrame = GameFrame.getInstance(500, gameController, showTest);

        gameFrame.setVisible(showTest);

        pause();

        assertTrue(gameController.startGame());

        pause();
    }

    @AfterEach
    public void afterEachTest() {
        if (showTest) {
            pause();
            gameFrame.setVisible(false);
        }
        pause();
    }

    @Test
    @Order(1)
    public void processTest1() {
        lose();

        returnToMenu();
    }

    @Test
    @Order(2)
    public void processTest2() {
        win();

        returnToMenu();
    }

    @Test
    @Order(3)
    public void processTest3() {
        lose();

        replayGame();

        win();

        replayGame();

        lose();

        replayGame();

        lose();

        returnToMenu();
    }

    @Test
    @Order(3)
    public void processTest4() {
        win();

        replayGame();

        win();

        returnToMenu();
    }

    // helper methods that are managing the paths in our process tests
    // many tests use the same code so this is a reduction for better structure
    public void pause() {
        try {
            Thread.sleep(this.pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void win() {
        assertTrue(gameController.isGameOn());
        assertFalse(gameController.isWasLevelWon());

        Player player = gameController.getPlayer();
        int playerXPosition = player.getXPosition();
        int playerYPosition = player.getYPosition();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

//        gameController.userTypeHandler(event);

        assertEquals(playerXPosition - 1, player.getXPosition());
        assertEquals(playerYPosition, player.getYPosition());

        pause();

//        gameController.userTypeHandler(event);

        assertEquals(playerXPosition - 2, player.getXPosition());
        assertEquals(playerYPosition, player.getYPosition());

        pause();

        while (gameController.isGameOn()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        assertFalse(gameController.isGameOn());
    }

    public void lose() {
        assertTrue(gameController.isGameOn());
        assertFalse(gameController.isWasLevelWon());

        Player player = gameController.getPlayer();
        int playerXPosition = player.getXPosition();
        int playerYPosition = player.getYPosition();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

//        gameController.userTypeHandler(event);

        assertEquals(playerXPosition + 1, player.getXPosition());
        assertEquals(playerYPosition, player.getYPosition());

        pause();

//        gameController.userTypeHandler(event);

        assertEquals(playerXPosition + 2, player.getXPosition());
        assertEquals(playerYPosition, player.getYPosition());

        pause();

        while (gameController.isGameOn()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        assertFalse(gameController.isGameOn());
    }

    public void replayGame() {
        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_G,  // Use KeyEvent.VK_R for the 'r' key
                'g'             // Specify the keychar as 'r'
        );

//        gameController.userTypeHandler(event);

        pause();

        assertTrue(gameController.isGameOn());
    }

    public void returnToMenu() {
        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_R,  // Use KeyEvent.VK_R for the 'r' key
                'r'             // Specify the keychar as 'r'
        );

//        gameController.userTypeHandler(event);

        pause();

        assertTrue(gameController.isMenuOpened());
    }
}
