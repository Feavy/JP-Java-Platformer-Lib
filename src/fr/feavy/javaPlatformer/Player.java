package fr.feavy.javaPlatformer;

public class Player extends Entity {

	public Player(float x, float y) {
		super(x, y, 28, 32);
	}

	@Override
	public void onCollision(Entity other, Side side) {
		other.setVelocityX(other.getVelocityX()-getVelocityX());
	}

}
