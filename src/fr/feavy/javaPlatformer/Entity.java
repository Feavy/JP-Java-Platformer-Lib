package fr.feavy.javaPlatformer;

import java.awt.Rectangle;

public abstract class Entity extends GameDrawable {

	private final float SPEED = 4f;
	private boolean isMoving = false;
	private Direction direction;

	private boolean isFrozen = false;
	
	private float velocityX = 0, velocityY = 0;
	
	private boolean isJumping = false;
	private boolean canJump = false;
	
	public Entity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public Rectangle getHitbox() {
		return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	}
	
	public float getVelocityX() {
		return velocityX;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
	
	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}
	
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}
	
	public void startMoving(Direction direction) {
		this.isMoving = true;
		this.direction = direction;
	}
	
	public void canJump() {
		this.canJump = true;
		if(isJumping)
			jump();
	}
	
	public void startJumping() {
		this.isJumping = true;
		if(canJump)
			jump();		
	}
	
	public void stopJumping() {
		this.isJumping = false;
	}
	
	public void stopMoving() {
		this.isMoving = false;
	}
	
	public void jump() {
		if(canJump) {
			this.velocityY = -6.5f;
			canJump = false;
		}
	}

	public abstract  void onCollision(Entity other, Side side);

	public void freeze() {
		this.isFrozen = true;
	}

	public void unFreeze() {
		this.isFrozen = false;
	}

	public boolean isFrozen() {
		return isFrozen;
	}

	@Override
	public void update() {
		if(isFrozen)
			return;

		this.setX(this.getX()+velocityX);
		this.setY(this.getY()+velocityY);
		
		if(canJump && velocityY > 0)
			canJump = false;
		
		if(isMoving && Math.abs(velocityX) < SPEED) {
			velocityX += (direction == Direction.LEFT) ? -0.1f : 0.1f;
		}else if(!isMoving && velocityX != 0) {
			if(Math.abs(velocityX) < .3f)
				velocityX = 0;
			else if(velocityX > 0) {
				velocityX -= 0.2f;
			}else {
				velocityX += 0.2f;
			}
		}
		
	}

}
