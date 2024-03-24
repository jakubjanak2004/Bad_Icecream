package Logic;

import BoardElements.VisitedNode;

import java.util.ArrayList;

public class ShortestPath {
    static String shortestPathThruMaze = "";
    static String shortestPath = "";
    static ArrayList<VisitedNode> VISITED_NODES_SHORTEST = new ArrayList<>();

    public static String getShortestPathStart(int x1, int y1, int x2, int y2){
        ShortestPath.getShortestPath(x1, y1, x2, y2);
        return ShortestPath.shortestPath;
    }
    public static String getShortestMazePathStart(int x1, int y1, int x2, int y2, String subPath, char was, int[][] boardArray, int numOfFields){
        shortestPathThruMaze = "";
        VISITED_NODES_SHORTEST.clear();

        getShortestPathThruMaze(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);

        return shortestPathThruMaze;
    }
    private static void getShortestPath(int x1, int y1, int x2, int y2){
        String path = "";

        int xDelta = x2 - x1;
        int yDelta = y2 - y1;

        String xSign = xDelta < 0? "l" : "r";
        String ySign = yDelta < 0? "u" : "d";

        for (int i = 0; i < Math.abs(xDelta); i++) {
            path += xSign;
        }

        for (int i = 0; i < Math.abs(yDelta); i++) {
            path += ySign;
        }

        shortestPath = path;
    }
    private static void getShortestPathThruMaze(int x1, int y1, int x2, int y2, String subPath, char was, int[][] boardArray, int numOfFields) {

        if (x1 == x2 && y1 == y2) {
            if (shortestPathThruMaze.isEmpty() || shortestPathThruMaze.length() > subPath.length()){
                shortestPathThruMaze = subPath;
            }
            return;
        }

        boolean haveOverwritten = false;
        for (VisitedNode node : VISITED_NODES_SHORTEST) {
            if (node.getXPosition() == x1 && node.getYPosition() == y1) {
                if (subPath.length() < node.getPathLength()){
                    node.setPath(subPath);
                    haveOverwritten = true;
                }else{
                    return;
                }
            }
        }

        if (!haveOverwritten) {
            VISITED_NODES_SHORTEST.add(new VisitedNode(x1, y1, subPath));
        }

        if (x1 - 1 >= 0 && boardArray[x1 - 1][y1] != 1 && boardArray[x1 - 1][y1] != 2 && was != 'r') {
            getShortestPathThruMaze(x1 - 1, y1, x2, y2, subPath + "l", 'l', boardArray, numOfFields);
        }
        if (x1 + 1 < numOfFields && boardArray[x1 + 1][y1] != 1 && boardArray[x1 + 1][y1] != 2 && was != 'l') {
            getShortestPathThruMaze(x1 + 1, y1, x2, y2, subPath + "r", 'r', boardArray, numOfFields);
        }
        if (y1 - 1 >= 0 && boardArray[x1][y1 - 1] != 1 && boardArray[x1][y1 - 1] != 2 && was != 'd') {
            getShortestPathThruMaze(x1, y1 - 1, x2, y2, subPath + "u", 'u', boardArray, numOfFields);
        }
        if (y1 + 1 < numOfFields && boardArray[x1][y1 + 1] != 1 && boardArray[x1][y1 + 1] != 2 && was != 'u') {
            getShortestPathThruMaze(x1, y1 + 1, x2, y2, subPath + "d", 'd', boardArray, numOfFields);
        }

    }
}
