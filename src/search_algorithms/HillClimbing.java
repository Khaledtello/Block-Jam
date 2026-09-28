package search_algorithms;

import models.Board;
import util.Node;

public class HillClimbing implements SearchAlgorithm {
    private int numberOfIterations;

    @Override
    public int numberOfIterations() {
        return this.numberOfIterations;
    }

    @Override
    public Node solve(Board board) {
        this.numberOfIterations = 0;
        Node current = new Node(board, null);
        int bestH = board.sumManhattanDistances();

        while (true) {
            board = current.board();

            Node best = null;

            for (Board successor : board.successors()) {
                int h = successor.sumManhattanDistances();
                if (h < bestH) {
                    bestH = h;
                    best = new Node(successor, current);
                }
            }

            if (best == null)
                return current;

            current = best;
            this.numberOfIterations++;
        }
    }
}
