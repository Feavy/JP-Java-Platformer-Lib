package fr.feavy.javaPlatformer;

public abstract class Entity extends GameDrawable implements Collidable{

	private final float SPEED = 4f;
	private boolean isMoving = false;
	private Direction direction;
	
	private float velocityX = 0, velocityY = 0;
	
	private boolean isJumping = false;
	private boolean canJump = false;
	
	public Entity(float x, float y, float width, float height) {
		super(x, y, width, height);
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
			isJumping = true;
			canJump = false;
		}
	}
	
	@Override
	public void update() {
		this.setX(this.getX()+velocityX);
		this.setY(this.getY()+velocityY);
		
		if(canJump && velocityY > 0)
			canJump = false;
		
		if(isMoving && Math.abs(velocityX) < SPEED) {
			velocityX += (direction == Direction.LEFT) ? -0.1f : 0.1f;
		}else if(!isMoving && velocityX != 0) {
			velocityX = 0;
		}
		
	}

}
