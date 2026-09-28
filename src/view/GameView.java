package view;

import controllers.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import search_algorithms.*;
import util.Algorithm;

import java.util.ArrayList;

public class GameView {
    private final Scene scene;

    public GameView(GameController controller) {
        BorderPane root = new BorderPane();

        final GridView grid = new GridView(controller);
        root.setCenter(grid);

        Label movesLabel = new Label("Moves: " + controller.getBoard().getNumberOfMoves());
        controller.setListener(board -> {
            grid.draw(board);
            movesLabel.setText("Moves: " + board.getNumberOfMoves());
        });

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> controller.restart());

        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> controller.undo());

        Button[] algoButtons = new Button[Algorithm.values().length];
        for (int i = 0; i < algoButtons.length; i++) {
            Algorithm algorithm = Algorithm.values()[i];
            algoButtons[i] = new Button("Solve By " + algorithm.name());
            algoButtons[i].setOnAction(e -> controller.solve(algorithm));
        }

        HBox top = new HBox(100, resetBtn, movesLabel, undoBtn);
        HBox bottom = new HBox(10, algoButtons);
        top.setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);
        root.setTop(top);
        root.setBottom(bottom);

        this.scene = new Scene(root, 800, 800);
    }

    public Scene scene() {
        return scene;
    }
}
