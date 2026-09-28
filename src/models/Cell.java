package models;

import util.AppColor;

public sealed interface Cell permits Block, Gate, Wall {
    AppColor color();
}
