package util;

import javafx.scene.paint.Color;

public enum AppColor {
    BLACK,
    RED,
    GREEN,
    YELLOW,
    BLUE,
    PURPLE,
    CYAN,
    ORANGE,
    WHITE,
    GRAY;

    public static AppColor fromNumber(int color) {
        return AppColor.values()[color];
    }

    public String ANSI()
    {
        for (int i = 0; i < AppColor.values().length && i < 7; i++)
            if (AppColor.values()[i] == this)
                return "\u001B[" + (30 + i) + 'm';
        return "\u001B[0m";
    }

    public Color fxColor() {
        return switch (this) {
            case BLACK  -> Color.BLACK;
            case RED    -> Color.RED;
            case GREEN  -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
            case BLUE   -> Color.BLUE;
            case PURPLE -> Color.PURPLE;
            case CYAN   -> Color.CYAN;
            case ORANGE -> Color.ORANGE;
            case WHITE  -> Color.WHITE;
            case GRAY   -> Color.GRAY;
        };
    }

    public String hex() {
        return String.format("%02X%02X%02X", (int)(this.fxColor().getRed()*255), (int)(this.fxColor().getGreen()*255), (int)(this.fxColor().getBlue()*255));
    }
}
