package fr.feavy.javaPlatformer;

public interface Collidable {

	public void onCollision(Collidable other);
	
	public float getX();
	
	public float getY();
	
	public float getWidth();
	
	public float getHeight();
}
