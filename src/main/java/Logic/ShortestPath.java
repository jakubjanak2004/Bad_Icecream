package Logic;

import BoardElements.VisitedNode;

import java.util.ArrayList;

public class ShortestPath {
    static String shortestPath = "";
    static ArrayList<VisitedNode> VISITED_NODES_SHORTEST = new ArrayList<>();
    public static String getShortestPathStart(int x1, int y1, int x2, int y2, String subPath, char was, int[][] boardArray, int numOfFields){
        shortestPath = "";
        VISITED_NODES_SHORTEST.clear();

        getShortestPath(x1, y1, x2, y2, subPath, was, boardArray, numOfFields);

        return shortestPath;
    }

    private static void getShortestPath(int x1, int y1, int x2, int y2, String subPath, char was, int[][] boardArray, int numOfFields) {

        if (x1 == x2 && y1 == y2) {
            if (shortestPath.isEmpty() || shortestPath.length() > subPath.length()){
                shortestPath = subPath;
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

        if (x1 - 1 >= 0 && boardArray[x1 - 1][y1] != 1 && was != 'r') {
            getShortestPath(x1 - 1, y1, x2, y2, subPath + "l", 'l', boardArray, numOfFields);
        }
        if (x1 + 1 < numOfFields && boardArray[x1 + 1][y1] != 1 && was != 'l') {
            getShortestPath(x1 + 1, y1, x2, y2, subPath + "r", 'r', boardArray, numOfFields);
        }
        if (y1 - 1 >= 0 && boardArray[x1][y1 - 1] != 1 && was != 'd') {
            getShortestPath(x1, y1 - 1, x2, y2, subPath + "u", 'u', boardArray, numOfFields);
        }
        if (y1 + 1 < numOfFields && boardArray[x1][y1 + 1] != 1 && was != 'u') {
            getShortestPath(x1, y1 + 1, x2, y2, subPath + "d", 'd', boardArray, numOfFields);
        }

    }
}
