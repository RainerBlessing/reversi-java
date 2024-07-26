package org.example;

public class Move {
    final Direction direction;
    final Coordinate start;
    final Coordinate end;

    public Move(Direction direction, Coordinate start, Coordinate end) {
        this.direction = direction;
        this.start = start;
        this.end = end;
    }

    public Move() {
        this.direction = Direction.INVALID;
        this.start = new Coordinate();
        this.end = new Coordinate();
    }

    public Move(Coordinate start,Coordinate end) {
        this.direction = Direction.INVALID;
        this.start = start;
        this.end = end;
    }

    public Move(char colCharStart, char rowCharStart, char colCharEnd, char rowCharEnd) {
        this.start = new Coordinate(colCharStart - 'A', rowCharStart - '1');
        this.end = new Coordinate(colCharEnd - 'A', rowCharEnd - '1');
        this.direction = determineDirection(start, end);
    }

    @Override
    public String toString() {
        return String.format("Direction: %s Start Coordinate: %s End Coordinate: %s",direction, start, end);
    }

    @Override
    public int hashCode() {
        return direction.hashCode()+ start.hashCode()+ end.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move that = (Move) o;
        return direction == that.direction && start.equals(that.start) && end.equals(that.end);
    }

    public int distance(){
        return (int)Math.sqrt(Math.pow(end.getX()- start.getX(),2)+Math.pow(end.getY()- start.getY(),2));
    }

    private Direction determineDirection(Coordinate start, Coordinate end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

        if (dx == 0 && dy < 0) return Direction.UP;
        if (dx == 0 && dy > 0) return Direction.DOWN;
        if (dx > 0 && dy == 0) return Direction.RIGHT;
        if (dx < 0 && dy == 0) return Direction.LEFT;
        if (dx > 0 && dy < 0) return Direction.UP_RIGHT;
        if (dx > 0) return Direction.DOWN_RIGHT;
        if (dx < 0 && dy < 0) return Direction.UP_LEFT;
        if (dx < 0) return Direction.DOWN_LEFT;

        throw new IllegalArgumentException("Invalid move: Start and end coordinates are the same");
    }
}
