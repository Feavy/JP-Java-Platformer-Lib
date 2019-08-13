package fr.feavy.javaPlatformer.map;

import fr.feavy.javaPlatformer.Entity;
import fr.feavy.javaPlatformer.Side;

import java.awt.*;

public abstract class Tile {
    private Color color;

    public Tile(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract void onCollision(Entity e, Side side);
}
