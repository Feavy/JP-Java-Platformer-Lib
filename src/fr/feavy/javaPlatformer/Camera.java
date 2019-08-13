package fr.feavy.javaPlatformer;

public class Camera extends GameDrawable {

	private Player player;
	
	public Camera(Player player, float width, float height) {
		super(0, 0, width, height);
		this.player = player;
	}

	@Override
	public void update() {
		this.setX(player.getX());
		this.setY(player.getY());
	}

}
