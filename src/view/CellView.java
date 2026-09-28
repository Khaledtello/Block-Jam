package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import models.Block;
import models.Cell;
import models.Gate;
import util.AppColor;
import util.Direction;

public class CellView extends StackPane {
    public CellView(Cell cell, int size, int exitedBlocks) {
        this.setPrefSize(size, size);

        Label label = new Label();
        StackPane.setAlignment(label, Pos.CENTER);
        label.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        this.getChildren().add(label);

        if (cell == null)
            this.setStyle("-fx-border-color: #999; -fx-background-color: #eee;");
        else
            this.setStyle("-fx-border-color: black; -fx-background-color: #" + cell.color().hex());

        if (cell instanceof Gate)
            label.setText("G");

        else if (cell instanceof Block block) {
            if (block.isFrozen(exitedBlocks)) {
                this.setStyle("-fx-border-color: black; -fx-background-color: #" + AppColor.WHITE.hex());
                label.setText("" + block.remainingBlocks(exitedBlocks));
                label.setStyle("-fx-font-size: 30px; -fx-text-fill: black;");
            }

            else if (block.allowedDirection() != Direction.BOTH)
                label.setText("" + block.allowedDirection().arrow());
        }
    }
}
