import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import BoardElements.rotationState.DownState;
import BoardElements.rotationState.LeftState;
import BoardElements.rotationState.RightState;
import BoardElements.rotationState.UpState;
import Logic.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.event.KeyEvent;
import java.util.Optional;

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
                {new BoardElement(0, 0), new BoardElement(0, 1), new IceBlock(0, 2), new BoardElement(0, 3)},
                {new IceBlock(1, 0), new BoardElement(1, 1), new BoardElement(1, 2), new BoardElement(1, 3)},
                {new BoardElement(2, 0), new BoardElement(2, 1), new BoardElement(2, 2), new BoardElement(2, 3)},
                {new BoardElement(3, 0), new BoardElement(3, 1), new BoardElement(3, 2), new BoardElement(3, 3)}
        };

        Optional<BoardElement>[][] boardArrayObjectOptional = new Optional[boardArrayObject.length][boardArrayObject[0].length];

        for (int i = 0; i < boardArrayObjectOptional.length; i++) {
            for (int j = 0; j < boardArrayObjectOptional[i].length; j++) {
                boardArrayObjectOptional[i][j] = Optional.ofNullable(boardArrayObject[i][j]);
            }
        }

        // pass the level to the Controller
        gameController.setBoardArrayObject(boardArrayObjectOptional);
        gameController.setNumOfFields(boardArrayObject.length);
    }

    @Test
    public void isRefreshing_whenControllerCreated_shouldBeRefreshingTheGUI() {
        assertTrue(gameController.isRefreshing());
    }

    @ParameterizedTest(name = "isVisitable at: x={0}, y={1}, expected={2}")
    @CsvSource({
            "0, 0, true",
            "0, 1, true",
            "0, 2, false",
            "0, 3, true",
            "1, 0, false",
            "1, 1, true",
            "1, 2, true",
            "1, 3, true",
            "2, 0, true",
            "2, 1, true",
            "2, 2, true",
            "2, 3, true",
            "3, 0, true",
            "3, 1, true",
            "3, 2, true",
            "3, 3, true",
            "0, -1, false",
            "0, 4, false",
            "1, -1, false",
            "1, 4, false",
            "2, -1, false",
            "2, 4, false",
            "3, -1, false",
            "3, 4, false",
            "-1, 0, false",
            "-1, 1, false",
            "-1, 2, false",
            "-1, 3, false",
            "4, 0, false",
            "4, 1, false",
            "4, 2, false",
            "4, 3, false"
    })
    public void isVisitableParametrized_getsValues_shouldNotCauseException(int x, int y, boolean expected) {
        assertEquals(expected, gameController.isVisitable(x, y));
    }

    @ParameterizedTest(name = "Get is block frozen at: x={0}, y={1}")
    @CsvSource({
            "0, 0, false",
            "0, 1, false",
            "0, 2, true",
            "0, 3, false",
            "1, 0, true",
            "1, 1, false",
            "1, 2, false",
            "1, 3, false",
            "2, 0, false",
            "2, 1, false",
            "2, 2, false",
            "2, 3, false",
            "3, 0, false",
            "3, 1, false",
            "3, 2, false",
            "3, 3, false",
            "0, -1, false",
            "0, 4, false",
            "1, -1, false",
            "1, 4, false",
            "2, -1, false",
            "2, 4, false",
            "3, -1, false",
            "3, 4, false",
            "-1, 0, false",
            "-1, 1, false",
            "-1, 2, false",
            "-1, 3, false",
            "4, 0, false",
            "4, 1, false",
            "4, 2, false",
            "4, 3, false"
    })
    public void isFrozenAtLocParametrized_getsValues_shouldNotCauseException(int x, int y, boolean expected) {
        assertEquals(expected, gameController.isFrozenAtLoc(x, y));
    }

    @ParameterizedTest(name = "Break the Ice at: x={0}, y={1}")
    @CsvSource({
            "0, 0",
            "0, 1",
            "0, 2",
            "0, 3",
            "1, 0",
            "1, 1",
            "1, 2",
            "1, 3",
            "2, 0",
            "2, 1",
            "2, 2",
            "2, 3",
            "3, 0",
            "3, 1",
            "3, 2",
            "3, 3",
            "0, -1",
            "0, 4",
            "1, -1",
            "1, 4",
            "2, -1",
            "2, 4",
            "3, -1",
            "3, 4",
            "-1, 0",
            "-1, 1",
            "-1, 2",
            "-1, 3",
            "4, 0",
            "4, 1",
            "4, 2",
            "4, 3"
    })
    public void beatIceTestParametrized_beatIceAtInvalidLoc_shouldNotCauseException(int x, int y) {
        gameController.beatIce(x, y);
    }

    @Test
    public void beatIce_beatIceAt10Loc_iceShouldBeDamaged() {
        int x = 1;
        int y = 0;
        gameController.beatIce(x, y);

        BoardElement bElement = gameController.getBoardArrayObject()[x][y].get();

        assertSame(bElement.getClass(), IceBlock.class);
        assertEquals(1, ((IceBlock) bElement).getStability());
    }

    @Test
    public void beatIce_beatIceAt10LocTwice_iceShouldBeBroken() {
        int x = 1;
        int y = 0;
        gameController.beatIce(x, y);
        gameController.beatIce(x, y);

        BoardElement bElement = gameController.getBoardArrayObject()[x][y].get();

        assertTrue(bElement.getClass() == BoardElement.class);
    }

    // described by the Test Scenario
    // Handler_1
//    @Test
//    public void userTypeHandler_stateGiven_ExpectedReturn() {
//        gameController.setGameOn(true);
//
//        KeyEvent e = new KeyEvent(gameController.getGameView(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
//
//        gameController.userTypeHandler(e);
//
//        Player player = gameController.getPlayer();
//
//        assertEquals(0, player.getXPosition());
//        assertEquals(RightState.class, player.getRotationState().getClass());
//    }

//    @Test
//    public void userTypeHandler_arrowDown_ExpectedReturn() {
//        gameController.setGameOn(true);
//
//        KeyEvent e = new KeyEvent(gameController.getGameView(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
//
//        gameController.userTypeHandler(e);
//
//        Player player = gameController.getPlayer();
//
//        assertEquals(1, player.getYPosition());
//        assertEquals(DownState.class, player.getRotationState().getClass());
//    }

//    @Test
//    public void userTypeHandler_arrowLeft_ExpectedReturn() {
//        gameController.setGameOn(true);
//
//        KeyEvent e = new KeyEvent(gameController.getGameView(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
//
//        gameController.userTypeHandler(e);
//
//        Player player = gameController.getPlayer();
//
//        assertEquals(0, player.getXPosition());
//        assertEquals(LeftState.class, player.getRotationState().getClass());
//    }

//    @Test
//    public void userTypeHandler_arrowUp_ExpectedReturn() {
//        gameController.setGameOn(true);
//
//        KeyEvent e = new KeyEvent(gameController.getGameView(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
//
//        gameController.userTypeHandler(e);
//
//        Player player = gameController.getPlayer();
//
//        assertEquals(0, player.getYPosition());
//        assertEquals(UpState.class, player.getRotationState().getClass());
//    }
}
