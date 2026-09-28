package models;

import util.AppColor;

public record Wall() implements Cell {
    @Override
    public AppColor color() {
        return AppColor.GRAY;
    }

    public String symbol() {
        return this.color().ANSI() + '*';
    }
}
