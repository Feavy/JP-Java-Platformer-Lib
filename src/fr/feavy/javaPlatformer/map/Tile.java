package fr.feavy.javaPlatformer.map;

import fr.feavy.javaPlatformer.drawable.Entity;
import fr.feavy.javaPlatformer.utils.Side;

import java.awt.*;
import java.util.Optional;

public abstract class Tile {
    public static final int WIDTH = 32;

    private Color color;

    public Tile(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract void onCollision(Entity e, Side side);

    public abstract void draw(Graphics2D g, Optional<Tile> topTile, Optional<Tile> rightTile, Optional<Tile> bottomTile, Optional<Tile> leftTile);
}
