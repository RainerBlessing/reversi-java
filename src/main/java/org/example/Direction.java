package org.example;

public enum Direction {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1),
    INVALID( 0, 0);

    public final int stepValueX;
    public final int stepValueY;

    Direction(int stepValueX, int stepValueY) {
        this.stepValueX = stepValueX;
        this.stepValueY = stepValueY;
    }
}