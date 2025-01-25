import BoardElements.Blocks.IceBlock;
import BoardElements.BoardElement.BoardElement;
import BoardElements.Player.Player;
import BoardElements.Player.Rotation;
import Logic.GameController;
import Logic.IceManipulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IceManipulatorTest {

    private IceManipulator iceManipulator;
    private GameController gameController;
    private Player player;
    private Optional<BoardElement>[][] boardArrayObjectOptional;

    private final BoardElement[][] boardArrayObjectAtStart = {
            {new BoardElement(0, 0), new BoardElement(0, 1), new IceBlock(0, 2), new BoardElement(0, 3)},
            {new IceBlock(1, 0), new BoardElement(1, 1), new BoardElement(1, 2), new BoardElement(1, 3)},
            {new BoardElement(2, 0), new BoardElement(2, 1), new BoardElement(2, 2), new BoardElement(2, 3)},
            {new BoardElement(3, 0), new BoardElement(3, 1), new BoardElement(3, 2), new BoardElement(3, 3)}
    };

    @BeforeEach
    public void setUp() {
        BoardElement[][] boardArrayObject = new BoardElement[][]{
                {new BoardElement(0, 0), new BoardElement(0, 1), new IceBlock(0, 2), new BoardElement(0, 3)},
                {new IceBlock(1, 0), new BoardElement(1, 1), new BoardElement(1, 2), new BoardElement(1, 3)},
                {new BoardElement(2, 0), new BoardElement(2, 1), new BoardElement(2, 2), new BoardElement(2, 3)},
                {new BoardElement(3, 0), new BoardElement(3, 1), new BoardElement(3, 2), new BoardElement(3, 3)}
        };

        boardArrayObjectOptional = new Optional[boardArrayObject.length][boardArrayObject[0].length];

        for (int i = 0; i < boardArrayObjectOptional.length; i++) {
            for (int j = 0; j < boardArrayObjectOptional[i].length; j++) {
                boardArrayObjectOptional[i][j] = Optional.ofNullable(boardArrayObject[i][j]);
            }
        }

        player = new Player(0, 0, Rotation.UP);

        // mocking the gameController
        gameController = mock(GameController.class);

        // returning desired objects from the mocked controller class
        when(gameController.getPlayer()).thenReturn(player);
//        when(gameController.getGameBoard()).thenReturn(boardArrayObjectOptional);
        when(gameController.getNumOfFields()).thenReturn(boardArrayObject.length);

        // creating the IceManipulator class instance
//        iceManipulator = new IceManipulator(gameController);
    }

    @Test
    public void manipulateIceAsyncTest_playerFacingWall_boardsShouldEqual() {
        this.player.setRot(Rotation.UP);

        iceManipulator.manipulateIce();

        BoardElement[][] boardArrayObjectToBeAsserted = new BoardElement[boardArrayObjectOptional.length][boardArrayObjectOptional[0].length];

        for (int i = 0; i < boardArrayObjectOptional.length; i++) {
            for (int j = 0; j < boardArrayObjectOptional[i].length; j++) {
                boardArrayObjectToBeAsserted[i][j] = boardArrayObjectOptional[i][j].get();
            }
        }

        assertTrue(Arrays.deepEquals(boardArrayObjectAtStart, boardArrayObjectToBeAsserted));
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldUnfreeze() {
        this.player.setRot(Rotation.RIGHT);

        iceManipulator.manipulateIce();

        assertFalse(boardArrayObjectOptional[1][0].get() instanceof IceBlock);
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldWholeRow() {
        this.player.setXPosition(3);
        this.player.setYPosition(0);
        this.player.setRot(Rotation.DOWN);

        iceManipulator.manipulateIce();

        assertEquals(new IceBlock(3, 1), boardArrayObjectOptional[3][1].get());
        assertEquals(new IceBlock(3, 2), boardArrayObjectOptional[3][2].get());
        assertEquals(new IceBlock(3, 3), boardArrayObjectOptional[3][3].get());
    }

    @Test
    public void manipulateIceAsyncTest_playerWillBeFreezing_shouldWholeColumn() {
        this.player.setXPosition(3);
        this.player.setYPosition(3);
        this.player.setRot(Rotation.LEFT);

        iceManipulator.manipulateIce();

        assertEquals(new IceBlock(2, 3), boardArrayObjectOptional[2][3].get());
        assertEquals(new IceBlock(1, 3), boardArrayObjectOptional[1][3].get());
        assertEquals(new IceBlock(0, 3), boardArrayObjectOptional[0][3].get());
    }
}
