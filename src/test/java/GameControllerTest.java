import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement;
import BoardElements.Player;
import Logic.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    GameController gameController;

    @BeforeEach
    public void setUp() {
        // set up the game controller object
        gameController = new GameController();

        // load synthetic level with some ice blocks
        // array[] -> x | array[][] -> y
        // indexes increasing -> down | indexes decreasing -> up
        BoardElement[][] boardArrayObject = {
                {null, null, new IceBlock(0, 2), null},
                {new IceBlock(1, 0), null, null, null},
                {null, null, null, null},
                {null, null, null, null}
        };

        // pass the level to the Controller
        gameController.setBoardArrayObject(boardArrayObject);
        gameController.setNumOfFields(boardArrayObject.length);
    }

    @Test
    public void isRefreshing_whenControllerCreated_shouldBeRefreshingTheGUI() {
        assertTrue(gameController.isRefreshing());
    }

    @ParameterizedTest(name = "isVisitable at: x={0}, y={1}, expected={2}")
    @CsvSource({
            "-1, -1, false",
            "0, -1, false",
            "-1, 0, false",
            "0, 0, true",
            "0, 1, true"
    })
    public void isVisitableParametrized_getsValues_shouldNotCauseException(int x, int y, boolean expected) {
        assertEquals(expected, gameController.isVisitable(x, y));
    }

    @ParameterizedTest(name = "Get is block frozen at: x={0}, y={1}")
    @CsvSource({
            "-1, -1, false",
            "0, -1, false",
            "-1, 0, false",
            "1, 0, true"
    })
    public void isFrozenAtLocParametrized_getsValues_shouldNotCauseException(int x, int y, boolean expected) {
        assertEquals(expected, gameController.isFrozenAtLoc(x, y));
    }

    @ParameterizedTest(name = "Break the Ice at: x={0}, y={1}")
    @CsvSource({
            "-1, -1",
            "0, -1",
            "-1, 0"
    })
    public void beatIceTestParametrized_beatIceAtNegativeLog_shouldNotCauseException(int x, int y) {
        gameController.beatIce(x, y);
    }

    @Test
    public void beatIce_beatIceAt00Loc_iceShouldBeDamaged() {
        int x = 1;
        int y = 0;
        gameController.beatIce(x, y);

        BoardElement bElement = gameController.getBoardArrayObject()[x][y];

        assertSame(bElement.getClass(), IceBlock.class);
        assertEquals(1, ((IceBlock) bElement).getStability());
    }

    @Test
    public void beatIce_beatIceAt00LocTwice_iceShouldBeBroken() {
        int x = 1;
        int y = 0;
        gameController.beatIce(x, y);
        gameController.beatIce(x, y);

        BoardElement bElement = gameController.getBoardArrayObject()[x][y];

        assertTrue(bElement != null && bElement.getClass() == BoardElement.class);
    }

    @Test
    public void userTypeHandler_stateGiven_ExpectedReturn() {
        gameController.setGameOn(true);

        KeyEvent e = new KeyEvent(gameController.getGAME_VIEW(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);

        gameController.userTypeHandler(e);

        Player player = gameController.getPLAYER();

        assertEquals(0, player.getXPosition());
        assertEquals(1, player.getRot());
    }

    @Test
    public void userTypeHandler_arrowDown_ExpectedReturn() {
        gameController.setGameOn(true);

        KeyEvent e = new KeyEvent(gameController.getGAME_VIEW(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);

        gameController.userTypeHandler(e);

        Player player = gameController.getPLAYER();

        assertEquals(1, player.getYPosition());
        assertEquals(2, player.getRot());
    }

    @Test
    public void userTypeHandler_arrowLeft_ExpectedReturn() {
        gameController.setGameOn(true);

        KeyEvent e = new KeyEvent(gameController.getGAME_VIEW(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);

        gameController.userTypeHandler(e);

        Player player = gameController.getPLAYER();

        assertEquals(0, player.getXPosition());
        assertEquals(3, player.getRot());
    }

    @Test
    public void userTypeHandler_arrowUp_ExpectedReturn() {
        gameController.setGameOn(true);

        KeyEvent e = new KeyEvent(gameController.getGAME_VIEW(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);

        gameController.userTypeHandler(e);

        Player player = gameController.getPLAYER();

        assertEquals(0, player.getYPosition());
        assertEquals(0, player.getRot());
    }
}
