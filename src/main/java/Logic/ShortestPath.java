package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.VisitedNode;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Shortest Path Algorithm class, this class contains algorithms for finding the shortest path in the game board,
 * taking ice into account.
 */
public class ShortestPath {
    private static final ArrayList<VisitedNode> VISITED_NOTES_SHORTEST_SOLID_BLOCKS = new ArrayList<>();
    private static final ArrayList<VisitedNode> VISITED_NODES_SHORTEST = new ArrayList<>();

    static String shortestPathThruMaze = "";
    static String shortestPath = "";

    private static void getShortestPath(int x1, int y1, int x2, int y2, String subPath, char was, Optional[][] boardArray, int numOfFields) {

        if (x1 == x2 && y1 == y2) {
            if (shortestPath.isEmpty() || shortestPath.length() > subPath.length()) {
                shortestPath = subPath;
            }
            return;
        }

        boolean haveOverwritten = false;
        for (VisitedNode node : VISITED_NOTES_SHORTEST_SOLID_BLOCKS) {
            if (node.getXPosition() == x1 && node.getYPosition() == y1) {
                if (subPath.length() < node.getPathLength()) {
                    node.setPath(subPath);
                    haveOverwritten = true;
                } else {
                    return;
                }
            }
        }

        if (!haveOverwritten) {
            VISITED_NOTES_SHORTEST_SOLID_BLOCKS.add(new VisitedNode(x1, y1, subPath));
        }

        if (x1 - 1 >= 0 && isNotSolidBlockOnLoc(x1 - 1, y1, boardArray) && was != 'r') {
            getShortestPath(x1 - 1, y1, x2, y2, subPath + "l", 'l', boardArray, numOfFields);
        }
        if (x1 + 1 < numOfFields && isNotSolidBlockOnLoc(x1 + 1, y1, boardArray) && was != 'l') {
            getShortestPath(x1 + 1, y1, x2, y2, subPath + "r", 'r', boardArray, numOfFields);
        }
        if (y1 - 1 >= 0 && isNotSolidBlockOnLoc(x1, y1 - 1, boardArray) && was != 'd') {
            getShortestPath(x1, y1 - 1, x2, y2, subPath + "u", 'u', boardArray, numOfFields);
        }
        if (y1 + 1 < numOfFields && isNotSolidBlockOnLoc(x1, y1 + 1, boardArray) && was != 'u') {
            getShortestPath(x1, y1 + 1, x2, y2, subPath + "d", 'd', boardArray, numOfFields);
        }
    }

    private static void getShortestPathThruMaze(int x1, int y1, int x2, int y2, String subPath, char was, Optional[][] boardArray, int numOfFields) {

        if (x1 == x2 && y1 == y2) {
            if (shortestPathThruMaze.isEmpty() || shortestPathThruMaze.length() > subPath.length()) {
                shortestPathThruMaze = subPath;
            }
            return;
        }

        boolean haveOverwritten = false;
        for (VisitedNode node : VISITED_NODES_SHORTEST) {
            if (node.getXPosition() == x1 && node.getYPosition() == y1) {
                if (subPath.length() < node.getPathLength()) {
                    node.setPath(subPath);
                    haveOverwritten = true;
                } else {
                    return;
                }
            }
        }

        if (!haveOverwritten) {
            VISITED_NODES_SHORTEST.add(new VisitedNode(x1, y1, subPath));
        }

        if (x1 - 1 >= 0 && isNotIceBlockOnLoc(x1 - 1, y1, boardArray) && isNotSolidBlockOnLoc(x1 - 1, y1, boardArray) && was != 'r') {
            getShortestPathThruMaze(x1 - 1, y1, x2, y2, subPath + "l", 'l', boardArray, numOfFields);
        }
        if (x1 + 1 < numOfFields && isNotIceBlockOnLoc(x1 + 1, y1, boardArray) && isNotSolidBlockOnLoc(x1 + 1, y1, boardArray) && was != 'l') {
            getShortestPathThruMaze(x1 + 1, y1, x2, y2, subPath + "r", 'r', boardArray, numOfFields);
        }
        if (y1 - 1 >= 0 && isNotIceBlockOnLoc(x1, y1 - 1, boardArray) && isNotSolidBlockOnLoc(x1, y1 - 1, boardArray) && was != 'd') {
            getShortestPathThruMaze(x1, y1 - 1, x2, y2, subPath + "u", 'u', boardArray, numOfFields);
        }
        if (y1 + 1 < numOfFields && isNotIceBlockOnLoc(x1, y1 + 1, boardArray) && isNotSolidBlockOnLoc(x1, y1 + 1, boardArray) && was != 'u') {
            getShortestPathThruMaze(x1, y1 + 1, x2, y2, subPath + "d", 'd', boardArray, numOfFields);
        }

    }

    /**
     * Returns the start of the shortest path. Used by strong monster to see what`s the shortest path.
     *
     * @param x1          x position on player board, source
     * @param y1          y position on player board, source
     * @param x2          x position on player board, target
     * @param y2          y position on player board, target
     * @param subPath     the path we have already taken, used in recursion
     * @param was         what was the last move
     * @param boardArray  the board array used in this game
     * @param numOfFields number of fields in rectangular board array
     * @return the first direction the board element should move
     */
    public static String getShortestPathStart(int x1, int y1, int x2, int y2, String subPath, char was, Optional[][] boardArray, int numOfFields) {
        shortestPath = "";
        VISITED_NOTES_SHORTEST_SOLID_BLOCKS.clear();

        ShortestPath.getShortestPath(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);
        return ShortestPath.shortestPath;
    }

    /**
     * Returns the start of the shortest path. Used by the clever monster to see if there is a path that leads to player.
     *
     * @param x1          x position on player board, source
     * @param y1          y position on player board, source
     * @param x2          x position on player board, target
     * @param y2          y position on player board, target
     * @param subPath     the path we have already taken, used in recursion
     * @param was         what was the last move
     * @param boardArray  the board array used in this game
     * @param numOfFields number of fields in rectangular board array
     * @return the first direction the board element should move
     */
    public static String getShortestMazePathStart(int x1, int y1, int x2, int y2, String subPath, char was, Optional[][] boardArray, int numOfFields) {
        shortestPathThruMaze = "";
        VISITED_NODES_SHORTEST.clear();

        getShortestPathThruMaze(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);

        return shortestPathThruMaze;
    }

    /**
     * This method returns if there is a solid block on a certain location.
     *
     * @param x                x coordinate
     * @param y                y coordinate
     * @param boardArrayObject the array containing the game Board as objects
     * @return true if there is no solid block on certain location
     */
    public static boolean isNotSolidBlockOnLoc(int x, int y, Optional[][] boardArrayObject) {
        if (x < 0 || x >= boardArrayObject.length || y < 0 || y >= boardArrayObject[0].length) {
            return true;
        }

        if (boardArrayObject[x][y].isPresent() && boardArrayObject[x][y].get().getClass() == SolidBlock.class) {
            return false;
        }
        return true;
    }

    /**
     * Returns if there is no ice block on location.
     *
     * @param x                x coordinate
     * @param y                y coordinate
     * @param boardArrayObject the array containing the game Board as objects
     * @return true if there is no ice block on certain location
     */
    public static boolean isNotIceBlockOnLoc(int x, int y, Optional[][] boardArrayObject) {
        if (x < 0 || x >= boardArrayObject.length || y < 0 || y >= boardArrayObject[0].length) {
            return false;
        }

        if (boardArrayObject[x][y].isPresent() && boardArrayObject[x][y].get().getClass() == IceBlock.class) {
            return false;
        }
        return true;
    }

}