package search_algorithms;

import models.Board;
import util.Algorithm;
import util.MoveResult;
import util.Node;

import java.util.*;

public class UCS_Greedy_AStar implements SearchAlgorithm {
    private int numberOfIterations;
    private final Algorithm algorithm;

    public UCS_Greedy_AStar(Algorithm algorithm) {
        if (algorithm != Algorithm.UCS && algorithm != Algorithm.GreedyBFS && algorithm != Algorithm.AStar)
            throw new IllegalArgumentException("Invalid Algorithm");
        this.algorithm = algorithm;
    }

    @Override
    public int numberOfIterations() {
        return this.numberOfIterations;
    }

    @Override
    public Node solve(Board board) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingInt(switch (algorithm) {
            case UCS -> Node::cost;
            case GreedyBFS -> Node::heuristic;
            default -> Node::f;
        }));

        nodes.add(new Node(board, null));

        HashMap<Board, Integer> bestCost = new HashMap<>();
        bestCost.put(board, 0);

        this.numberOfIterations = 0;

        while (!nodes.isEmpty()) {
            this.numberOfIterations++;

            Node node = nodes.poll();
            board = node.board();

            if (board.isSolved())
                return node;

            for (MoveResult move : board.successorsWithMovedBlocks()) {
                Board successor = move.board();

                int stepCost = 1;
                if (algorithm == Algorithm.UCS) {
                    stepCost += move.block().cost();
                    if (board.getExitedBlocks() < successor.getExitedBlocks())
                        stepCost = -100;
                }

                int newCost = node.cost() + stepCost;
                Integer oldCost = bestCost.get(successor);

                if (oldCost == null || newCost < oldCost) {
                    bestCost.put(successor, newCost);
                    int heuristic = algorithm == Algorithm.UCS ? 0 : successor.sumManhattanDistances();
                    nodes.add(new Node(successor, node, newCost, heuristic));
                }
            }
        }

        return null;
    }
}
