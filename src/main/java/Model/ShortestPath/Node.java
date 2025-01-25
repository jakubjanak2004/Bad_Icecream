package Model.ShortestPath;

import Model.Block.IceBlock;
import Model.BoardElement.BoardElement;
import Model.Player.Rotation;

public class Node {
    private int length;
    private int xPosition;
    private int yPosition;
    private Node previousNode;
    private Rotation jumpToNodeRotation;

    public Node(BoardElement boardElement) {
        this.xPosition = boardElement.getXPosition();
        this.yPosition = boardElement.getYPosition();
        this.length = 0;
    }

    public Node(BoardElement boardElement, Node previousNode) {
        this.xPosition = boardElement.getXPosition();
        this.yPosition = boardElement.getYPosition();
        this.previousNode = previousNode;

        if (previousNode == null) {
            return;
        }

        if (boardElement instanceof IceBlock) {
            this.length = previousNode.length + 2;
        } else {
            this.length = previousNode.length + 1;
        }

        int xDifference = getXPosition() - previousNode.getXPosition();
        int yDifference = getYPosition() - previousNode.getYPosition();

        if (xDifference == 1) jumpToNodeRotation = Rotation.RIGHT;
        if (xDifference == -1) jumpToNodeRotation = Rotation.LEFT;
        if (yDifference == 1) jumpToNodeRotation = Rotation.DOWN;
        if (yDifference == -1) jumpToNodeRotation = Rotation.UP;
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

    public Rotation getJumpToNodeRotation() {
        return jumpToNodeRotation;
    }

    public int getLength() {
        return length;
    }
}

