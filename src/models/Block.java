package models;

import util.AppColor;
import util.Direction;
import util.Move;

import util.Position;

import java.util.concurrent.atomic.AtomicInteger;

public record Block(int id, AppColor color, Position[] positions, Direction allowedDirection, int threshold) implements Cell {
    private static final AtomicInteger GEN = new AtomicInteger();

    public Block(AppColor color, Position[] positions) {
        this(GEN.getAndIncrement(), color, positions, Direction.BOTH, 0);
    }

    public Block(AppColor color, Position[] positions,  Direction allowedDirection, int threshold) {
        this(GEN.getAndIncrement(), color, positions, allowedDirection, threshold);
    }

    public boolean isFrozen(int exitedBlocks) {
        return exitedBlocks < this.threshold;
    }

    public int remainingBlocks(int exitedBlocks) {
        return Math.max(0, this.threshold - exitedBlocks);
    }

    public boolean canMove(Move move, int exitedBlocks) {
        if (this.isFrozen(exitedBlocks)) return false;

        return switch (this.allowedDirection) {
            case HORIZONTAL -> move == Move.RIGHT || move == Move.LEFT;
            case VERTICAL -> move == Move.UP || move == Move.DOWN;
            case BOTH -> true;
        };
    }

    public int cost() {
        return this.positions.length + (this.allowedDirection == Direction.BOTH ? 1 : 0);
    }

    public Block copy() {
        Position[] copy = new Position[this.positions.length];
        for (int i = 0; i < this.positions.length; i++) {
            Position p = this.positions[i];
            copy[i] = new Position(p.x(), p.y());
        }
        return new Block(this.id, this.color, copy, this.allowedDirection, this.threshold);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block that)) return false;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public String coloredSymbol(int exitedBlocks) {
        if (this.isFrozen(exitedBlocks))
            return AppColor.WHITE.ANSI() + this.remainingBlocks(exitedBlocks);

        return this.color.ANSI() + this.allowedDirection.arrow();
    }
}
