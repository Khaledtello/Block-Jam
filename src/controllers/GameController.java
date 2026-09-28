package controllers;

import javafx.application.Platform;
import models.Board;
import search_algorithms.*;
import util.Algorithm;
import util.Move;
import util.Node;
import util.Position;

import java.util.*;
import java.util.function.Consumer;

public class GameController {
    private Consumer<Board> listener;
    private final Stack<Board> boards;

    public GameController(Board board) {
        this.boards = new Stack<>();
        this.boards.push(board);
    }

    public void setListener(Consumer<Board> listener) {
        this.listener = listener;
    }

    private void updateBoard(Board board) {
        if (listener != null) listener.accept(board);
    }


    public boolean moveBlock(Position position, Move move) {
        Board board = this.getBoard().copy();
        if (board.moveBlock(position, move)) {
            this.boards.push(board);
            this.updateBoard(board);
            return true;
        }
        return false;
    }

    public void undo() {
        if (this.boards.size() > 1) {
            this.boards.pop();
            this.updateBoard(this.getBoard());
        }
    }

    public void restart() {
        while (this.boards.size() > 1)
            this.boards.pop();
        this.updateBoard(this.getBoard());
    }

    public Board getBoard() {
        return this.boards.peek();
    }

    public void solve(Algorithm algorithm) {
        SearchAlgorithm sa = switch (algorithm) {
            case BFS, DFS -> new DFS_BFS(algorithm);
            case RDFS -> new RDFS();
            case HillClimbing -> new HillClimbing();
            case UCS, GreedyBFS, AStar -> new UCS_Greedy_AStar(algorithm);
        };

        new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            long beforeTime = System.currentTimeMillis();

            Node node = sa.solve(this.getBoard());

            long time = System.currentTimeMillis() - beforeTime;
            long memory = (runtime.totalMemory() - runtime.freeMemory() - beforeMemory) / 1024;
            Stack<Board> road = this.backtrace(node);

            this.printStats(algorithm.name(), time, memory, sa.numberOfIterations(), road.size());
            this.drawRoad(road);
        }).start();
    }

    private Stack<Board> backtrace(Node node) {
        Stack<Board> road = new Stack<>();
        while (node != null && node.parent() != null) {
            road.push(node.board());
            node = node.parent();
        }

        return road;
    }

    private void drawRoad(Stack<Board> road) {
        int moves = road.size();
        long delay = moves == 0 ? 0 : Math.max(5 * 1000 / moves, 1);

        while (!road.isEmpty()) {
            Board board = road.pop();
            this.boards.push(board);
            Platform.runLater(() -> this.updateBoard(board));
            try { Thread.sleep(delay); } catch (Exception ignored) {}
        }
    }

    private void printStats(String name, long timeMs, long memoryKB, int iterations, int moves) {
        System.out.printf(
                "Algorithm: %s | Time: %d ms | Memory: %d KB | Iterations: %d | Moves: %d%n",
                name,
                timeMs,
                memoryKB,
                iterations,
                moves
        );
    }
}
