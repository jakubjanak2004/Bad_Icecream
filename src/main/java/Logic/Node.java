package Logic;

import BoardElements.Rotation;

public class Node {
    private int xPosition;
    private int yPosition;
    private Node previousNode;
    private Rotation previousNodeRotation;

    public Node(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Node(int xPosition, int yPosition, Node previousNode) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.previousNode = previousNode;

        if (previousNode == null) {
            return;
        }

        int xDifference = getXPosition() - previousNode.getXPosition();
        int yDifference = getYPosition() - previousNode.getYPosition();

        if (xDifference == 1) previousNodeRotation = Rotation.RIGHT;
        if (xDifference == -1) previousNodeRotation = Rotation.LEFT;
        if (yDifference == 1) previousNodeRotation = Rotation.DOWN;
        if (yDifference == -1) previousNodeRotation = Rotation.UP;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node node)) {
            return false;
        }
        if (node.getXPosition() != xPosition) {
            return false;
        }
        if (node.getYPosition() != yPosition) {
            return false;
        }
        return true;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public Rotation getPreviousNodeRotation() {
        return previousNodeRotation;
    }
}

