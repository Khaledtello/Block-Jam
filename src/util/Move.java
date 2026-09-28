package util;

public enum Move {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    public final int dx, dy;

    Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Position apply(Position position) {
        return new Position(position.x() + dx, position.y() + dy);
    }
}
