package search_algorithms;

import models.Board;
import util.Node;

import java.util.HashSet;

public class RDFS implements SearchAlgorithm {
    private HashSet<Board> visited;

    @Override
    public int numberOfIterations() {
        return this.visited.size();
    }

    @Override
    public Node solve(Board board) {
        this.visited = new HashSet<>();
        return this.solve(new Node(board, null));
    }

    private Node solve(Node node) {
        Board board = node.board();

        if (board.isSolved())
            return node;

        if (!this.visited.add(board))
            return null;

        for (Board successor : board.successors()) {
            Node result = this.solve(new Node(successor, node));
            if (result != null)
                return result;
        }

        return null;
    }
}
