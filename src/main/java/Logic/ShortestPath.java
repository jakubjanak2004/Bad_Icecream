package Logic;

import BoardElements.Blocks.Block;
import BoardElements.Blocks.IceBlock;
import BoardElements.Blocks.SolidBlock;
import BoardElements.BoardElement;
import BoardElements.Rotation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

/**
 * Shortest Path Algorithm class, this class contains algorithms for finding the shortest path in the game board,
 * taking ice into account.
 */
public class ShortestPath {
    private static Rotation AStartRotation = Rotation.NEUTRAL;

    /**
     * Shortest path with ice
     *
     * @param x1         x position on player board, source
     * @param y1         y position on player board, source
     * @param x2         x position on player board, target
     * @param y2         y position on player board, target
     * @param boardArray the board array used in this game
     * @return the first direction the board element should move
     */
    public static Rotation getShortestPathNoIceStart(int x1, int y1, int x2, int y2, Optional<BoardElement>[][] boardArray) {
        AStartShortestPathConditional(x1, y1, x2, y2, boardArray, boardElement ->
                (boardArray[boardElement.getXPosition()][boardElement.getYPosition()].get() instanceof Block)
                        &&
                        !(boardArray[boardElement.getXPosition()][boardElement.getYPosition()].get() instanceof IceBlock));
        return AStartRotation;
    }

    /**
     * Shortest path with no ice
     *
     * @param x1         x position on player board, source
     * @param y1         y position on player board, source
     * @param x2         x position on player board, target
     * @param y2         y position on player board, target
     * @param boardArray the board array used in this game
     * @return the first direction the board element should move
     */
    public static Rotation getShortestPathWithIceStart(int x1, int y1, int x2, int y2, Optional<BoardElement>[][] boardArray) {
        AStartShortestPathConditional(x1, y1, x2, y2, boardArray, boardElement -> boardArray[boardElement.getXPosition()][boardElement.getYPosition()].get() instanceof Block);
        return AStartRotation;
    }

    /**
     * This method returns if there is a solid block on a certain location.
     *
     * @param x                x coordinate
     * @param y                y coordinate
     * @param boardArrayObject the array containing the game Board as objects
     * @return true if there is no solid block on certain location
     */
    public static boolean isNotSolidBlockOnLocation(int x, int y, Optional[][] boardArrayObject) {
        if (x < 0 || x >= boardArrayObject.length || y < 0 || y >= boardArrayObject[0].length) {
            return true;
        }

        return boardArrayObject[x][y].isEmpty() || boardArrayObject[x][y].get().getClass() != SolidBlock.class;
    }

    /**
     * Returns if there is no ice block on location.
     *
     * @param x                x coordinate
     * @param y                y coordinate
     * @param boardArrayObject the array containing the game Board as objects
     * @return true if there is no ice block on certain location
     */
    public static boolean isNotIceBlockOnLocation(int x, int y, Optional[][] boardArrayObject) {
        if (x < 0 || x >= boardArrayObject.length || y < 0 || y >= boardArrayObject[0].length) {
            return false;
        }

        return boardArrayObject[x][y].isEmpty() || boardArrayObject[x][y].get().getClass() != IceBlock.class;
    }

    private static void AStartShortestPathConditional(int x1, int y1, int x2, int y2, Optional<BoardElement>[][] boardArray, Function<Node, Boolean> boardElementConditions) {
        List<Node> visitedNodes = new ArrayList<>();
        Queue<Node> toBeVisitedNodes = new PriorityQueue<>(
                Comparator.comparingDouble(node -> ShortestPath.fCost(x1, y1, node.getXPosition(), node.getYPosition(), x2, y2))
        );
        toBeVisitedNodes.add(new Node(x1, y1));

        Node previousNode = null;
        while (!toBeVisitedNodes.isEmpty()) {
            Node visitingNode = toBeVisitedNodes.poll();
            if (visitingNode.getXPosition() == x2 && visitingNode.getYPosition() == y2) {
                setRotation(visitingNode);
                return;
            }
            // adding not visited neighbour into toBeVisitedNodes
            getAllNeighbours(visitingNode.getXPosition(), visitingNode.getYPosition(), boardArray).forEach(neighbour -> {
                if (boardArray[neighbour.getXPosition()][neighbour.getYPosition()].isPresent()) {
                    if (boardElementConditions.apply(neighbour)) {
                        return;
                    }
                }
                if (!visitedNodes.contains(new Node(neighbour.getXPosition(), neighbour.getYPosition()))) {
                    toBeVisitedNodes.add(new Node(neighbour.getXPosition(), neighbour.getYPosition(), visitingNode));
                }
            });
            Node addNode = new Node(visitingNode.getXPosition(), visitingNode.getYPosition(), previousNode);
            visitedNodes.add(addNode);
            previousNode = visitingNode;
        }
        AStartRotation = Rotation.NEUTRAL;
    }

    // helper functions for the A* algo
    private static int gCost(int xStart, int yStart, int xCurrent, int yCurrent) {
        return Math.abs(xStart - xCurrent) + Math.abs(yStart - yCurrent);
    }

    private static int hCost(int xCurrent, int yCurrent, int xFinish, int yFinish) {
        return Math.abs(xCurrent - xFinish) + Math.abs(yCurrent - yFinish);
    }

    private static int fCost(int xStart, int yStart, int xCurrent, int yCurrent, int xFinish, int yFinish) {
        return gCost(xStart, yStart, xCurrent, yCurrent) + hCost(xCurrent, yCurrent, xFinish, yFinish);
    }

    private static List<Node> getAllNeighbours(int x, int y, Optional<BoardElement>[][] boardArray) {
        List<Node> neighbours = new ArrayList<>();
        if (x + 1 < boardArray.length) neighbours.add(new Node(x + 1, y));
        if (x - 1 >= 0) neighbours.add(new Node(x - 1, y));
        if (y + 1 < boardArray[0].length) neighbours.add(new Node(x, y + 1));
        if (y - 1 >= 0) neighbours.add(new Node(x, y - 1));
        return neighbours;
    }

    private static void setRotation(Node previousNode) {
        while (previousNode != null) {
            if (previousNode.getPreviousNode() != null) {
                if (previousNode.getPreviousNode().getPreviousNode() == null) {
                    AStartRotation = previousNode.getPreviousNodeRotation();
                    return;
                }
            }
            previousNode = previousNode.getPreviousNode();
        }
    }
}