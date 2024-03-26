package Logic;

import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.VisitedNode;

import java.util.ArrayList;

public class ShortestPath {

    static String shortestPathThruMaze = "";
    static String shortestPath = "";

    static ArrayList<VisitedNode> VISITED_NODES_SHORTEST = new ArrayList<>();
    static ArrayList<VisitedNode> VISITED_NOTES_SHORTEST_SOLID_BLOCKS = new ArrayList<>();

    public static String getShortestPathStart(int x1, int y1, int x2, int y2, String subPath, char was, BoardElement[][] boardArray, int numOfFields) {
        shortestPath = "";
        VISITED_NOTES_SHORTEST_SOLID_BLOCKS.clear();

        ShortestPath.getShortestPath(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);
        return ShortestPath.shortestPath;
    }

    private static void getShortestPath(int x1, int y1, int x2, int y2, String subPath, char was, BoardElement[][] boardArray, int numOfFields) {

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

    public static String getShortestMazePathStart(int x1, int y1, int x2, int y2, String subPath, char was, BoardElement[][] boardArray, int numOfFields) {
        shortestPathThruMaze = "";
        VISITED_NODES_SHORTEST.clear();

        getShortestPathThruMaze(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);

        return shortestPathThruMaze;
    }

    private static void getShortestPathThruMaze(int x1, int y1, int x2, int y2, String subPath, char was, BoardElement[][] boardArray, int numOfFields) {

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

    public static boolean isNotSolidBlockOnLoc(int x, int y, BoardElement[][] boardArrayObject) {
        if (boardArrayObject[x][y] != null && boardArrayObject[x][y].getClass() == SolidBlock.class) {
            return false;
        }
        return true;
    }

    public static boolean isNotIceBlockOnLoc(int x, int y, BoardElement[][] boardArrayObject) {
        if (boardArrayObject[x][y] != null && boardArrayObject[x][y].getClass() == IceBlock.class) {
            return false;
        }
        return true;
    }

}