package fr.feavy.javaPlatformer.system;

import fr.feavy.javaPlatformer.drawable.Entity;

public class Camera extends GameObject {

	private Entity player;
	
	public Camera(Entity player, float width, float height) {
		super(0, 0, width, height);
		this.player = player;
	}

	@Override
	public void update() {
		this.setX(player.getX());
		this.setY(player.getY());
	}

}
