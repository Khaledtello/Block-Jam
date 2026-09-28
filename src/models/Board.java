package models;

import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class Board {
    private final Grid grid;
    private final ArrayList<Block> blocks;
    private final HashMap<AppColor, ArrayList<Gate>> gatesByColor;
    private int exitedBlocks;
    private int numberOfMoves;
    private long hash;

    public Board(int rows, int cols, HashMap<AppColor, ArrayList<Gate>> gatesByColor) {
        this.grid = new Grid(rows, cols);
        this.blocks = new ArrayList<>();
        this.gatesByColor = gatesByColor;
        this.exitedBlocks = 0;
        this.numberOfMoves = 0;
    }

    public Board(int rows, int cols) {
        this(rows, cols, new HashMap<>());
    }

    public int rows() {
        return this.grid.rows;
    }

    public int cols() {
        return this.grid.cols;
    }

    public int getNumberOfBlocks() {
        return this.blocks.size();
    }

    public int getExitedBlocks() {
        return this.exitedBlocks;
    }

    public int getNumberOfMoves() {
        return this.numberOfMoves;
    }

    public Cell cellAt(int x, int y) {
        return this.grid.cellAt(new Position(x, y));
    }

    public Cell cellAt(Position position) {
        return this.grid.cellAt(position);
    }

    public void add(Position position, Wall wall) {
        this.grid.placeContent(position, wall);
    }

    public void add(Gate gate) {
        ArrayList<Gate> gates = this.gatesByColor.computeIfAbsent(gate.color(), k -> new ArrayList<>());
        gates.add(gate);
        for (Position position : gate.positions())
            this.grid.placeContent(position, gate);
    }

    public void add(Block block) {
        this.blocks.add(block);
        for (Position position : block.positions())
            this.grid.placeContent(position, block);
    }

    public void removeBlock(Block block) {
        this.grid.delete(block);
        this.blocks.remove(block);
        this.exitedBlocks++;
    }

    public boolean isSolved() {
        return this.blocks.isEmpty();
    }

    public boolean moveBlock(Position position, Move move) {
        Cell cell = this.cellAt(position);
        if (cell instanceof Block block)
            return this.moveBlock(block, move);

        return false;
    }

    private boolean moveBlock(Block block, Move move) {
        if (!this.blocks.contains(block))
            return false;

        if (!block.canMove(move, this.exitedBlocks))
            return false;

        if (!this.grid.isCollision(block, move))
            return false;

        for (Position position : block.positions())
            this.hash ^= Zobrist.TABLE[position.x()][position.y()][this.hashCell(position.x(),  position.y())];

        this.grid.moveBlock(block, move);
        this.numberOfMoves++;

        if (this.grid.canBlockEnterGate(block))
            this.removeBlock(block);

        else
            for (Position position : block.positions())
                this.hash ^= Zobrist.TABLE[position.x()][position.y()][this.hashCell(position.x(),  position.y())];

        return true;
    }

    public Board copy() {
        Board copy = new Board(this.rows(), this.cols(), this.gatesByColor);
        for (int i = 0; i < this.rows(); i++)
            for (int j = 0; j < this.cols(); j++) {
                Position pos = new Position(i, j);
                Cell cell = this.cellAt(pos);
                if (!(cell instanceof Block))
                    copy.grid.placeContent(pos, cell);
            }

        copy.exitedBlocks = this.exitedBlocks;
        copy.numberOfMoves = this.numberOfMoves;
        copy.hash = this.hash;
        for (Block block : this.blocks)
            copy.add(block.copy());

        return copy;
    }

    public ArrayList<Board> successors() {
        ArrayList<Board> successors = new ArrayList<>();
        for (Move move : Move.values())
            for (int i = 0; i < this.blocks.size(); i++) {
                Board board = this.copy();
                if (board.moveBlock(board.blocks.get(i), move))
                    successors.add(board);
            }

        return successors;
    }

    public ArrayList<MoveResult> successorsWithMovedBlocks() {
        ArrayList<MoveResult> successors = new ArrayList<>();
        for (Move move : Move.values())
            for (int i = 0; i < this.blocks.size(); i++) {
                Board board = this.copy();
                Block block = board.blocks.get(i);
                if (board.moveBlock(block, move))
                    successors.add(new MoveResult(board, block));
            }

        return successors;
    }

    public int sumManhattanDistances() {
        int sum = 0;
        for (Block block : this.blocks) {
            int min = Integer.MAX_VALUE;
            for (Gate gate : this.gatesByColor.get(block.color())) {
                int md = Math.abs(block.positions()[0].x() - gate.positions()[0].x()) + Math.abs(block.positions()[0].y() - gate.positions()[0].y());
                min = Math.min(min, md);
            }

            sum += min;
        }

        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Board that)) return false;

        if (this.rows() != that.rows() || this.cols() != that.cols())
            return false;

        for (int i = 0; i < this.rows(); i++) {
            for (int j = 0; j < this.cols(); j++) {
                Cell c1 = this.cellAt(i, j);
                Cell c2 = that.cellAt(i, j);
                if (!Objects.equals(c1, c2))
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getHash());
    }

    public long getHash() {
        if (this.hash == 0) {
            for (int i = 0; i < this.rows(); i++)
                for (int j = 0; j < this.cols(); j++) {
                    int t = hashCell(i, j);
                    if (t != 0)
                        hash ^= Zobrist.TABLE[i][j][t];
                }
        }

        return this.hash;
    }

    private int hashCell(int x, int y) {
        Cell c = this.cellAt(x, y);
        if (c instanceof Wall)
            return 1;

        if (c instanceof Gate g)
            return g.color().ordinal();

        if (c instanceof Block b)
            return b.id() + 50;

        return 0;
    }

    public void printGrid() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.rows(); i++) {
            for (int j = 0; j < this.cols(); j++) {
                Cell cell = this.cellAt(i, j);
                s.append(' ');
                if (cell == null)
                    s.append(' ');
                else if (cell instanceof Block block)
                    s.append(block.coloredSymbol(this.exitedBlocks));
                else if (cell instanceof Gate gate)
                    s.append(gate.symbol());
                else if  (cell instanceof Wall wall)
                    s.append(wall.symbol());
            }
            s.append("\n");
        }

        System.out.println(s);
    }
}
