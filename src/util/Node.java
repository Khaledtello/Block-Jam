package util;

import models.Board;

public record Node(Board board, Node parent, int cost, int heuristic) {
    public Node(Board board, Node parent) {
        this(board, parent, 0, 0);
    }

    public Node(Board board, Node parent, int cost) {
        this(board, parent, cost, 0);
    }
//
//    public int h() {
//        return this.board.manhattanDistance() + this.board.getNumberOfBlocks() * 20;
//    }

    public int f() {
        return this.cost + this.heuristic;
    }
}

