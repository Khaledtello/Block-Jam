package models;

import util.Move;
import util.Position;

public final class Grid {
    public final int rows, cols;
    private final Cell[][] cells;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
    }

    public Cell cellAt(Position position) {
        return this.cells[position.x()][position.y()];
    }

    public Cell nextCell(Position position, Move move) {
        return this.cellAt(move.apply(position));
    }

    public boolean isOccupied(Position position) {
        return this.cellAt(position) != null;
    }

    public boolean isEmpty(Position position) {
        return this.cellAt(position) == null;
    }

    private void validateEmpty(Position position) {
        if (this.isOccupied(position))
            throw new IllegalStateException("Cell already occupied at (" + position.x() + "," + position.y() + ")");
    }

    public void placeContent(Position position, Cell content) {
//        this.validateEmpty(position);
        this.cells[position.x()][position.y()] = content;
    }

    public boolean isCollision(Block block, Move move) {
        for (Position position : block.positions()) {
            Cell nextCell = this.nextCell(position, move);
            if (nextCell != null && nextCell != block)
                return false;
        }

        return true;
    }

    public void moveBlock(Block block, Move move) {
        Position[] positions = block.positions();

        for (Position position : positions)
            this.cells[position.x()][position.y()] = null;

        for (int i = 0; i < positions.length; i++) {
            Position targetPosition = move.apply(positions[i]);
            this.cells[targetPosition.x()][targetPosition.y()] = block;
            positions[i] = targetPosition;
        }
    }

    public boolean canBlockEnterGate(Block block) {
        Position[] positions = block.positions();
        for (Move move : Move.values()) {
            boolean allMatch = false;
            for (Position position : positions)
                if (this.nextCell(position, move) instanceof Gate gate && gate.color() == block.color())
                    allMatch = true;

            if (allMatch) {
                for (Position position : positions) {
                    if (!allMatch)
                        break;

                    Position target = move.apply(position);
                    while (true) {
                        Cell cell = this.cellAt(target);
                        target = move.apply(target);
                        if (cell == null || cell == block) continue;

                        if (!(cell instanceof Gate gate && gate.color() == block.color()))
                            allMatch = false;

                        break;
                    }
                }
            }

            if (allMatch)
                return true;
        }

        return false;
    }

    public void delete(Block block) {
        for (Position position : block.positions())
            this.cells[position.x()][position.y()] = null;
    }
}
