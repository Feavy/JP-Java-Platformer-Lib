package fr.feavy.javaPlatformer.map;

import fr.feavy.javaPlatformer.Entity;
import fr.feavy.javaPlatformer.Side;

import java.awt.*;

public class BasicTile extends Tile{
    public BasicTile(Color color) {
        super(color);
    }

    @Override
    public void onCollision(Entity e, Side side) {}
}
