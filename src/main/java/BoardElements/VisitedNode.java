package BoardElements;

public class VisitedNode implements IsOn2DimensionalBoard {
    private final int X_POSITION;
    private final int Y_POSITION;
    private int pathLength;

    @Override
    public int getXPosition() {
        return X_POSITION;
    }

    @Override
    public int getYPosition() {
        return Y_POSITION;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPath(String path) {
        this.pathLength = path.length();
    }

    public VisitedNode(int x, int y, String path) {
        this.X_POSITION = x;
        this.Y_POSITION = y;
        this.pathLength = path.length();
    }
}
