package fr.feavy.javaPlatformer.drawable;

import fr.feavy.javaPlatformer.utils.Side;

import java.awt.*;

public abstract class SimpleEntity extends Entity {
    public SimpleEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0x673ab7));
        g.fillRoundRect(0, 0, (int)getWidth(), (int)getHeight(), 20, 20);
    }
}
