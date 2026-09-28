import controllers.GameController;
import models.Board;
import javafx.application.Application;
import javafx.stage.Stage;
import util.Zobrist;
import view.GameView;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        Board board = GameInitializer.test();
        Board board = GameInitializer.fromJson("boards/board1.json");
        Zobrist.init(board.rows(), board.cols(), board.getNumberOfBlocks());
        GameController controller = new GameController(board);
        GameView view = new GameView(controller);

        stage.setTitle("Color Block Jam");
        stage.setScene(view.scene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}