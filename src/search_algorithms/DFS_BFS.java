package search_algorithms;

import models.Board;
import util.Algorithm;
import util.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

public class DFS_BFS implements SearchAlgorithm {
    private int numberOfIterations;
    private final Algorithm algorithm;

    public DFS_BFS(Algorithm algorithm) {
        if (algorithm != Algorithm.DFS && algorithm != Algorithm.BFS)
            throw new IllegalArgumentException("Invalid Algorithm");
        this.algorithm = algorithm;
    }

    @Override
    public int numberOfIterations() {
        return this.numberOfIterations;
    }

    @Override
    public Node solve(Board board) {
        Deque<Node> nodes = new ArrayDeque<>();
        nodes.add(new Node(board, null));

        HashSet<Board> visited = new HashSet<>();
        visited.add(board);

        this.numberOfIterations = 0;
        int exitedBlocks = 0;

        while (!nodes.isEmpty()) {
            this.numberOfIterations++;

            Node node = algorithm == Algorithm.DFS ? nodes.removeLast() : nodes.removeFirst();
            board = node.board();

            if (board.isSolved())
                return node;

            if (algorithm == Algorithm.BFS && board.getExitedBlocks() > exitedBlocks) {
                exitedBlocks = board.getExitedBlocks();
                nodes.clear();
                visited.clear();
            }

            for (Board successor : board.successors())
                if (visited.add(successor))
                    nodes.add(new Node(successor, node));
        }

        return null;
    }
}
