import com.google.gson.*;
import models.Block;
import models.Board;
import models.Gate;
import models.Wall;
import util.AppColor;
import util.Direction;
import util.Position;

import java.io.FileReader;
import java.io.IOException;

public class GameInitializer {
    public static Board test() {
        Board board = new Board(4, 4);
        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                if (i == 0 || j == 0 || i == 3 || j == 3)
                    board.add(new Position(i, j), new Wall());

        board.add(new Position(2, 2), new Wall());
//        board.add(new Position(0, 1), new Gate(AppColor.RED));
//        board.add(new Position(3, 1), new Gate(AppColor.BLUE));
        board.add(new Block(AppColor.BLUE, new Position[]{new Position(1, 2)}));
        board.add(new Block(AppColor.RED, new Position[]{new Position(2, 1)}));
        return board;
    }

    public static Board fromJson(String filePath) throws IOException {
        JsonObject jo = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();

        Board board = new Board(jo.get("rows").getAsInt(), jo.get("cols").getAsInt());

        for (JsonElement cor : jo.getAsJsonArray("blocks"))
            board.add(new Position(cor.getAsJsonArray().get(0).getAsInt(), cor.getAsJsonArray().get(1).getAsInt()), new Wall());

        for (JsonElement element : jo.getAsJsonArray("exists")) {
            JsonObject gate = element.getAsJsonObject();
            board.add(new Gate(AppColor.fromNumber(gate.get("color").getAsInt()), toPositions(gate)));
        }

        for (JsonElement element : jo.getAsJsonArray("shapes")) {
            JsonObject block = element.getAsJsonObject();

            String direction = "both";
            if (block.has("direction"))
                direction = block.get("direction").getAsString();

            int threshold = 0;
            if (block.has("move_lock"))
                threshold = block.get("move_lock").getAsInt();

            board.add(new Block(AppColor.fromNumber(block.get("colors").getAsInt()), toPositions(block), Direction.fromString(direction), threshold));
        }

        return board;
    }

    private static Position[] toPositions(JsonObject jsonObject) {
        JsonArray coordinates = jsonObject.get("coordinates").getAsJsonArray();
        Position[] positions = new Position[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++)
            positions[i] = new Position(coordinates.get(i).getAsJsonArray().get(0).getAsInt(), coordinates.get(i).getAsJsonArray().get(1).getAsInt());

        return positions;
    }
}

