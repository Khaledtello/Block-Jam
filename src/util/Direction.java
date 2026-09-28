package util;

public enum Direction {
    HORIZONTAL,
    VERTICAL,
    BOTH;

    public static Direction fromString(String value) {
        return switch (value) {
            case "horizontal" -> HORIZONTAL;
            case "vertical" -> VERTICAL;
            default -> BOTH;
        };
    }

    public char arrow() {
        return switch (this) {
            case HORIZONTAL -> '↔';
            case VERTICAL   -> '↕';
            case BOTH       -> 'B';
        };
    }
}
