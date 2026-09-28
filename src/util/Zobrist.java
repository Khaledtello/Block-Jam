package util;

import java.util.Random;

public final class Zobrist {
    public static long[][][] TABLE;

    public static void init(int rows, int cols, int blocksNumber) {
        Random r = new Random(123456789);
        int maxTypes = blocksNumber + 50;
        TABLE = new long[rows][cols][maxTypes];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                for (int t = 0; t < maxTypes; t++)
                    TABLE[i][j][t] = r.nextLong();
    }
}

