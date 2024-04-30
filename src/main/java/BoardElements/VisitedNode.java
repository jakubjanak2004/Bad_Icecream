package BoardElements;

/**
 * Visited node is an object representation of an node on the graph that the Shortest Path Algo already visited.
 * This graph represents the game board and is used to find the shortest path.
 */
public class VisitedNode implements IsOn2DimensionalBoard {
    private final int X_POSITION;
    private final int Y_POSITION;
    private int pathLength;

    public VisitedNode(int x, int y, String path) {
        this.X_POSITION = x;
        this.Y_POSITION = y;
        this.pathLength = path.length();
    }

    /**
     * Get the length of a path that led to this point. It is the shortest path that was discovered until this node of time by the algorithm.
     * @return the length of a path that led to this node
     */
    public int getPathLength() {
        return pathLength;
    }

    /**
     * If the path discovered by this iteration in the algo was shorter than the shortest yet discovered we can change the path length.
     * @param path the path that the algo is iterating through
     */
    public void setPath(String path) {
        this.pathLength = path.length();
    }

    @Override
    public int getXPosition() {
        return X_POSITION;
    }

    @Override
    public int getYPosition() {
        return Y_POSITION;
    }
}
