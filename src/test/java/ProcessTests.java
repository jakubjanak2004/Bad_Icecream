import LevelManagement.Level;
import LevelManagement.LevelManager;
import Logic.GameController;
import View.GameFrame;
import View.GameView;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessTests {

    // set the pause between the steps as well as if the viw will be Visible
    int pause = 500;
    boolean showTest = true;

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

        gameView = new GameView(gameController);
        gameFrame = new GameFrame(500, gameController, showTest);

        if (!showTest) {
            gameFrame.setVisible(false);
        }

        pause();

        gameController.startGame();

        pause();
    }

    @AfterEach
    public void afterEachTest() {
        if (showTest) {
            pause();
            gameFrame.dispose();
        }
        pause();
    }

    @Test
    @Order(1)
    public void processTest1() {
        KeyEvent event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_R,  // Use KeyEvent.VK_R for the 'r' key
                'r'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);
    }

    @Test
    @Order(2)
    public void processTest2() {
        KeyEvent event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_R,  // Use KeyEvent.VK_R for the 'r' key
                'r'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);
    }

    @Test
    @Order(3)
    public void processTest3() {
        KeyEvent event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_G,  // Use KeyEvent.VK_R for the 'g' key
                'g'             // Specify the keychar as 'g'
        );

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_G,  // Use KeyEvent.VK_R for the 'r' key
                'g'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_G,  // Use KeyEvent.VK_R for the 'r' key
                'g'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_R,  // Use KeyEvent.VK_R for the 'r' key
                'r'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);
    }

    @Test
    @Order(3)
    public void processTest4() {
        KeyEvent event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_G,  // Use KeyEvent.VK_R for the 'r' key
                'g'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);
        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        gameController.userTypeHandler(event);

        pause();

        gameController.userTypeHandler(event);

        pause();

        event = new KeyEvent(
                gameFrame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_R,  // Use KeyEvent.VK_R for the 'r' key
                'r'             // Specify the keychar as 'r'
        );

        gameController.userTypeHandler(event);
    }

    public void pause() {
        try {
            Thread.sleep(this.pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
