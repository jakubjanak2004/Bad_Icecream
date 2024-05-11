import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import Logic.ShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathTest {

    BoardElement[][] boardArrayObject;

    @BeforeEach
    public void setUp() {
        // load synthetic level with some ice blocks
        // array[] -> x | array[][] -> y
        // indexes increasing -> down | indexes decreasing -> up
        boardArrayObject = new BoardElement[][]{
                {null, null, new IceBlock(0, 2), null},
                {new IceBlock(1, 0), null, null, null},
                {null, null, null, null},
                {null, null, null, new SolidBlock(3, 3)}
        };
    }

    @ParameterizedTest(name = "isNotIceBlockOnLoc at: x={0}, y={1}, expected={2}")
    @CsvSource({
            "-1, -1, false",
            "0, -1, false",
            "-1, 0, false",
            "0, 0, true",
            "0, 1, true",
            "0, 2, false"
    })
    public void isNotIceBlockOnLocTest_Parametrized(int x, int y, boolean expected) {
        assertEquals(expected, ShortestPath.isNotIceBlockOnLoc(x, y, boardArrayObject));
    }

    @ParameterizedTest(name = "isNotIceBlockOnLoc at: x={0}, y={1}, expected={2}")
    @CsvSource({
            "-1, -1, true",
            "0, -1, true",
            "-1, 0, true",
            "0, 0, true",
            "0, 1, true",
            "3, 3, false"
    })
    public void isNotSolidBlockOnLocTest_Parametrized(int x, int y, boolean expected) {
        assertEquals(expected, ShortestPath.isNotSolidBlockOnLoc(x, y, boardArrayObject));
    }

    @ParameterizedTest(name = "getShortestMazePathStart at: x1={0}, y1={1}, x2={2}, y2={3}, path={4}")
    @CsvSource({
            "0, 0, 0, 0, ''",
            "3, 0, 0, 0, ldllu",
            "0, 0, 3, 1, drrr"
    })
    public void getShortestMazePathStart_StartAndFinishGiven_PathShouldMatch(int x1, int y1, int x2, int y2, String path) {
        String pathFromAlgo = ShortestPath.getShortestMazePathStart(x1, y1, x2, y2, "", 'e', boardArrayObject, boardArrayObject.length);

        assertEquals(path, pathFromAlgo);
    }

    @ParameterizedTest(name = "getShortestMazePathStart at: x1={0}, y1={1}, x2={2}, y2={3}, path={4}")
    @CsvSource({
            "0, 0, 0, 0, ''",
            "3, 0, 0, 0, lll",
            "0, 0, 3, 1, rrrd"
    })
    public void getShortestPathStart_StartAndFinishGiven_PathShouldMatch(int x1, int y1, int x2, int y2, String path) {
        String pathFromAlgo = ShortestPath.getShortestPathStart(x1, y1, x2, y2, "", 'e', boardArrayObject, boardArrayObject.length);

        assertEquals(path, pathFromAlgo);
    }

}
