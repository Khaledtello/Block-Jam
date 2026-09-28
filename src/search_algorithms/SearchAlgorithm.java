package search_algorithms;

import models.Board;
import util.Node;

public interface SearchAlgorithm {
    int numberOfIterations();
    Node solve(Board board);
}
