import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement;
import BoardElements.Player;
import BoardElements.Rotation;
import Logic.GameController;
import Logic.IceManipulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IceManipulatorTest {

    private IceManipulator iceManipulator;
    private GameController gameController;
    private Player player;
    private BoardElement[][] boardArrayObject;

    private final BoardElement[][] boardArrayObjectAtStart = {
            {new BoardElement(0, 0), new BoardElement(0, 1), new IceBlock(0, 2), new BoardElement(0, 3)},
            {new IceBlock(1, 0), new BoardElement(1, 1), new BoardElement(1, 2), new BoardElement(1, 3)},
            {new BoardElement(2, 0), new BoardElement(2, 1), new BoardElement(2, 2), new BoardElement(2, 3)},
            {new BoardElement(3, 0), new BoardElement(3, 1), new BoardElement(3, 2), new BoardElement(3, 3)}
    };

    @BeforeEach
    public void setUp() {
        boardArrayObject = new BoardElement[][]{
                {new BoardElement(0, 0), new BoardElement(0, 1), new IceBlock(0, 2), new BoardElement(0, 3)},
                {new IceBlock(1, 0), new BoardElement(1, 1), new BoardElement(1, 2), new BoardElement(1, 3)},
                {new BoardElement(2, 0), new BoardElement(2, 1), new BoardElement(2, 2), new BoardElement(2, 3)},
                {new BoardElement(3, 0), new BoardElement(3, 1), new BoardElement(3, 2), new BoardElement(3, 3)}
        };
        player = new Player(0, 0, Rotation.UP);

        // mocking the gameController
        gameController = mock(GameController.class);

        // returning desired objects from the mocked controller class
        when(gameController.getPLAYER()).thenReturn(player);
        when(gameController.getBoardArrayObject()).thenReturn(boardArrayObject);
        when(gameController.getNumOfFields()).thenReturn(boardArrayObject.length);

        // creating the IceManipulator class instance
        iceManipulator = new IceManipulator(gameController);
    }

    @Test
    public void manipulateIceAsyncTest_playerFacingWall_boardsShouldEqual() {
        this.player.setRot(Rotation.UP);

        iceManipulator.manipulateIce();

        assertTrue(Arrays.deepEquals(boardArrayObjectAtStart, boardArrayObject));
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldUnfreeze() {
        this.player.setRot(Rotation.RIGHT);

        iceManipulator.manipulateIce();

        assertFalse(boardArrayObject[1][0] instanceof IceBlock);
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldWholeRow() {
        this.player.setXPosition(3);
        this.player.setYPosition(0);
        this.player.setRot(Rotation.DOWN);

        iceManipulator.manipulateIce();

        assertEquals(new IceBlock(3, 1), boardArrayObject[3][1]);
        assertEquals(new IceBlock(3, 2), boardArrayObject[3][2]);
        assertEquals(new IceBlock(3, 3), boardArrayObject[3][3]);
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldWholeColumn() {
        this.player.setXPosition(3);
        this.player.setYPosition(3);
        this.player.setRot(Rotation.LEFT);

        iceManipulator.manipulateIce();

        assertEquals(new IceBlock(2, 3), boardArrayObject[2][3]);
        assertEquals(new IceBlock(1, 3), boardArrayObject[1][3]);
        assertEquals(new IceBlock(0, 3), boardArrayObject[0][3]);
    }
}
