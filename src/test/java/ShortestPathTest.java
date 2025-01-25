//import Model.Blocks.IceBlock;
//import Model.Blocks.SolidBlock;
//import Model.BoardElement.BoardElement;
//import Model.Player.Rotation;
//import Model.ShortestPath.ShortestPath;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ShortestPathTest {
//
//    Optional<BoardElement>[][] boardArrayObjectOptional;
//
//    @BeforeEach
//    public void setUp() {
//        // load synthetic level with some ice blocks
//        // array[] -> x | array[][] -> y
//        // indexes increasing -> down | indexes decreasing -> up
//        BoardElement[][] boardArrayObject = new BoardElement[][]{
//                {null, null, new IceBlock(0, 2), null},
//                {new IceBlock(1, 0), null, null, null},
//                {null, null, null, null},
//                {null, null, null, new SolidBlock(3, 3)}
//        };
//
//        boardArrayObjectOptional = new Optional[boardArrayObject.length][boardArrayObject[0].length];
//
//        for (int i = 0; i < boardArrayObjectOptional.length; i++) {
//            for (int j = 0; j < boardArrayObjectOptional[i].length; j++) {
//                boardArrayObjectOptional[i][j] = Optional.ofNullable(boardArrayObject[i][j]);
//            }
//        }
//    }
//
//    @ParameterizedTest(name = "isNotIceBlockOnLoc at: x={0}, y={1}, expected={2}")
//    @CsvSource({
//            "-1, -1, false",
//            "0, -1, false",
//            "-1, 0, false",
//            "0, 0, true",
//            "0, 1, true",
//            "0, 2, false"
//    })
//    public void isNotIceBlockOnLocTest_Parametrized(int x, int y, boolean expected) {
//        assertEquals(expected, ShortestPath.isNotIceBlockOnLocation(x, y, boardArrayObjectOptional));
//    }
//
//    @ParameterizedTest(name = "isNotIceBlockOnLoc at: x={0}, y={1}, expected={2}")
//    @CsvSource({
//            "-1, -1, true",
//            "0, -1, true",
//            "-1, 0, true",
//            "0, 0, true",
//            "0, 1, true",
//            "3, 3, false"
//    })
//    public void isNotSolidBlockOnLocTest_Parametrized(int x, int y, boolean expected) {
//        assertEquals(expected, ShortestPath.isNotSolidBlockOnLocation(x, y, boardArrayObjectOptional));
//    }
//
//    @ParameterizedTest(name = "getShortestMazePathStart at: x1={0}, y1={1}, x2={2}, y2={3}, path={4}")
//    @CsvSource({
//            "0, 0, 0, 0, NEUTRAL",
//            "3, 0, 0, 0, LEFT",
//            "0, 0, 3, 1, DOWN"
//    })
//    public void getShortestMazePathStart_StartAndFinishGiven_PathShouldMatch(int x1, int y1, int x2, int y2, Rotation path) {
//        Rotation rot = ShortestPath.getPathStartNoIce(x1, y1, x2, y2, boardArrayObjectOptional);
//
//        assertEquals(path, rot);
//    }
//
//    @ParameterizedTest(name = "getShortestMazePathStart at: x1={0}, y1={1}, x2={2}, y2={3}, path={4}")
//    @CsvSource({
//            "0, 0, 0, 0, NEUTRAL",
//            "3, 0, 0, 0, LEFT",
//            "0, 0, 3, 1, RIGHT"
//    })
//    public void getShortestPathStart_StartAndFinishGiven_PathShouldMatch(int x1, int y1, int x2, int y2, Rotation path) {
//        Rotation rotation = ShortestPath.getPathStartWithIce(x1, y1, x2, y2, boardArrayObjectOptional);
//
//        assertEquals(path, rotation);
//    }
//
//}
