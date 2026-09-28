package view;

import controllers.GameController;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import models.Board;
import util.Move;
import util.Position;

public class GridView extends GridPane {
    private final GameController controller;
    private final int CELL_SIZE = 50;
    private Position pressedPos;
    private double dragX;
    private double dragY;

    GridView(GameController controller) {
        this.controller = controller;

        this.setAlignment(Pos.CENTER);
        this.draw(controller.getBoard());
        this.setOnMouseDragged(this::onMouseDragged);
        this.setOnMouseReleased(e -> this.pressedPos = null);
    }

    public void draw(Board board) {
        this.getChildren().clear();

        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                CellView cell = new CellView(board.cellAt(i, j), CELL_SIZE, board.getExitedBlocks());
                this.add(cell, j, i);
                int r = i;
                int c = j;
                cell.setOnMousePressed(e -> {
                    this.dragX = e.getSceneX();
                    this.dragY = e.getSceneY();
                    this.pressedPos = new Position(r, c);
                });
            }
    }

    public final void onMouseDragged(MouseEvent e) {
        if (this.pressedPos == null)
            return;

        double dx = e.getSceneX() - this.dragX;
        double dy = e.getSceneY() - this.dragY;

        Move move = null;

        if (Math.abs(dx) >= this.CELL_SIZE) {
            move = dx >= this.CELL_SIZE ? Move.RIGHT : Move.LEFT;
            this.dragX = e.getSceneX();
        }

        if (Math.abs(dy) >= this.CELL_SIZE) {
            move = dy >= this.CELL_SIZE ? Move.DOWN : Move.UP;
            this.dragY = e.getSceneY();
        }

        if (move != null && this.controller.moveBlock(this.pressedPos, move))
            this.pressedPos = move.apply(this.pressedPos);
    }
}
