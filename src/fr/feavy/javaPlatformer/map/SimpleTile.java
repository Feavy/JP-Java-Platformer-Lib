package fr.feavy.javaPlatformer.map;

import fr.feavy.javaPlatformer.drawable.Entity;
import fr.feavy.javaPlatformer.utils.Side;

import java.awt.*;
import java.util.Optional;

public class SimpleTile extends Tile {
    public SimpleTile(Color color) {
        super(color);
    }

    @Override
    public void onCollision(Entity e, Side side) {}

    @Override
    public void draw(Graphics2D g, Optional<Tile> topTile, Optional<Tile> rightTile, Optional<Tile> bottomTile, Optional<Tile> leftTile) {
        g.setColor(getColor());
        g.fillRoundRect(0, 0, WIDTH, WIDTH, 20, 20);

        if (topTile.isPresent())
            g.fillRect(0, 0, WIDTH, 20);

        if (rightTile.isPresent())
            g.fillRect(WIDTH - 20, 0, 20, WIDTH);

        if (bottomTile.isPresent())
            g.fillRect(0, WIDTH - 20, WIDTH, 20);

        if (leftTile.isPresent())
            g.fillRect(0, 0, 20, WIDTH);

    }
}
