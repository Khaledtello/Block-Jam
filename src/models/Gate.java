package models;

import util.AppColor;
import util.Position;

public record Gate(AppColor color, Position[] positions) implements Cell {
    public String symbol() {
        return color.ANSI() + 'G';
    }
}
