package BoardElements.Monsters;

import BoardElements.BoardElement;

import java.awt.*;

public class Monster extends BoardElement {
    public enum Type {
        STUPID,
        CLEVER,
        STRONG
    }
    protected Type monsterType;
    protected Color color;

    public Color getColor() {
        return this.color;
    }

    public Type getMonsterType() {
        return this.monsterType;
    }

    protected Monster(int xPosition, int yPosition, int rot) {
        super(xPosition, yPosition, rot);
    }

    public void move(boolean canUp, boolean canRight, boolean canDown, boolean canLeft, int tries) {
        if (tries > 3) return;
        if (rot == 0) {
            if (canUp) {
                super.yPosition -= 1;
            } else {
                super.rot = 1;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 1) {
            if (canRight) {
                super.xPosition += 1;
            } else {
                super.rot = 2;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 2) {
            if (canDown) {
                super.yPosition += 1;
            } else {
                super.rot = 3;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        } else if (rot == 3) {
            if (canLeft) {
                super.xPosition -= 1;
            } else {
                super.rot = 0;
                move(canUp, canRight, canDown, canLeft, (tries + 1));
            }
        }
    }

    public void moveTo(char positionChar) {
        switch (positionChar) {
            case 'u':
                this.rot = 0;
                break;
            case 'r':
                this.rot = 1;
                break;
            case 'd':
                this.rot = 2;
                break;
            case 'l':
                this.rot = 3;
                break;
        }
        move(true, true, true, true, 0);
    }
}