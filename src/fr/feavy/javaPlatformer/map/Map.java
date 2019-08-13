package fr.feavy.javaPlatformer.map;

import java.io.*;
import java.util.Optional;

public class Map {
    private String name;
    private int width, height;
    private int[][] blocks;
    private Tile[] tiles;

    public Map(String name, int width, int height, int[][] blocks, Tile[] tiles) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.blocks = blocks;
        this.tiles = tiles;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Optional<Tile> getTile(int x, int y) {
        try {
            if (this.blocks[y][x] == 0)
                return Optional.ofNullable(null);
            else
                return Optional.ofNullable(tiles[this.blocks[y][x] - 1]);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }

    public Optional<Tile> getTile(float x, float y, int tileWidth) {
        return getTile((int)(x/ tileWidth), (int)(y/ tileWidth));
    }

    public static Map fromFile(String path, Tile[] tiles) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Map.class.getClass().getResourceAsStream(path)));

        int width = Integer.parseInt(reader.readLine());
        int height = Integer.parseInt(reader.readLine());

        int[][] blocks = new int[height][width];

        String line;

        for (int i = 0; i < height; i++) {
            line = reader.readLine();
            for (int j = 0; j < width; j++) {
                blocks[i][j] = Integer.parseInt(line.charAt(j) + "");
            }
        }

        System.out.println(width + ", " + height);
        return new Map("", width, height, blocks, tiles);
    }
}
