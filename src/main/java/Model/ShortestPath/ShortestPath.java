package Model.ShortestPath;

import Model.Block.Block;
import Model.Block.IceBlock;
import Model.Block.SolidBlock;
import Model.BoardElement.BoardElement;
import Model.GameBoard.GameBoard;
import Model.Player.Rotation;

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
//    private static Rotation AStartRotation = Rotation.NEUTRAL;

    /**
     * Shortest path with ice
     *
     * @param x1         x position on player board, source
     * @param y1         y position on player board, source
     * @param x2         x position on player board, target
     * @param y2         y position on player board, target
     * @return the first direction the board element should move
     */
    public static Rotation getPathStartWithIce(int x1, int y1, int x2, int y2, GameBoard gameBoard) {
        return AStarShortestPathConditional(x1, y1, x2, y2, gameBoard, boardElement ->
                (gameBoard.getBoardElementAt(boardElement.getXPosition(), boardElement.getYPosition()).get() instanceof Block)
                        &&
                        !(gameBoard.getBoardElementAt(boardElement.getXPosition(), boardElement.getYPosition()).get() instanceof IceBlock));
    }

    /**
     * Shortest path with no ice
     *
     * @param x1         x position on player board, source
     * @param y1         y position on player board, source
     * @param x2         x position on player board, target
     * @param y2         y position on player board, target
     * @return the first direction the board element should move
     */
    public static Rotation getPathStartNoIce(int x1, int y1, int x2, int y2, GameBoard gameBoard) {
        return AStarShortestPathConditional(x1, y1, x2, y2, gameBoard, boardElement -> gameBoard.getBoardElementAt(boardElement.getXPosition(), boardElement.getYPosition()).get() instanceof Block);
    }

    // TODO: A* can end up in a loop, maybe try dijkstra again
    private static Rotation AStarShortestPathConditional(int x1, int y1, int x2, int y2, GameBoard gameBoard, Function<Node, Boolean> boardElementConditions) {
        List<Node> visitedNodes = new ArrayList<>();
        Queue<Node> toBeVisitedNodes = new PriorityQueue<>(
                Comparator.comparingDouble(node -> ShortestPath.fCost(x1, y1, node.getXPosition(), node.getYPosition(), x2, y2))
        );
        toBeVisitedNodes.add(new Node(x1, y1));

        Node previousNode = null;
        while (!toBeVisitedNodes.isEmpty()) {
            Node visitingNode = toBeVisitedNodes.poll();
            if (visitingNode.getXPosition() == x2 && visitingNode.getYPosition() == y2) {
                return setRotation(visitingNode);
            }
            // adding not visited neighbour into toBeVisitedNodes
            getAllNeighbours(visitingNode.getXPosition(), visitingNode.getYPosition(), gameBoard).forEach(neighbour -> {
                if (gameBoard.getBoardElementAt(neighbour.getXPosition(), neighbour.getYPosition()).isPresent()) {
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
        return Rotation.NEUTRAL;
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

    private static List<Node> getAllNeighbours(int x, int y, GameBoard gameBoard) {
        List<Node> neighbours = new ArrayList<>();
        if (x + 1 < gameBoard.getGameBoardLengthX()) neighbours.add(new Node(x + 1, y));
        if (x - 1 >= 0) neighbours.add(new Node(x - 1, y));
        if (y + 1 < gameBoard.getGameBoardLengthY()) neighbours.add(new Node(x, y + 1));
        if (y - 1 >= 0) neighbours.add(new Node(x, y - 1));
        return neighbours;
    }

    private static Rotation setRotation(Node previousNode) {
        while (previousNode != null) {
            if (previousNode.getPreviousNode() != null) {
                if (previousNode.getPreviousNode().getPreviousNode() == null) {
                    return previousNode.getJumpToNodeRotation();
                }
            }
            previousNode = previousNode.getPreviousNode();
        }
        return Rotation.NEUTRAL;
    }
}