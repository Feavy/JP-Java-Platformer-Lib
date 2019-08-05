package fr.feavy.javaPlatformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Main gamePanel = new Main(60);
		JFrame frame = new JFrame("JP - Java Platformer");
		frame.addKeyListener(gamePanel.getKeyListener());
		frame.setContentPane(gamePanel);
		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	
	public final int WIDTH = 32;
	
	private final float FORCE = 9.8f;
	
	private int width, height;
	private int[][] map;
	
	private List<Entity> entities = new ArrayList<>();
	
	private KeyListener keyListener;
	
	private Camera camera;
	private Player player;
	
	public Main(int fps) throws NumberFormatException, IOException {
		this.player = new Player(0, 0);
		this.camera = new Camera(player, 800, 800);
		
		this.keyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case 37:	// Gauche
					player.stopMoving();
					break;
				case 38:	// Haut
					player.stopJumping();
					break;
				case 39:	// Droite
					player.stopMoving();
					break;
				default:
					break;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case 37:	// Gauche
					player.startMoving(Direction.LEFT);
					break;
				case 38:	// Haut
					player.startJumping();
					break;
				case 39:	// Droite
					player.startMoving(Direction.RIGHT);
					break;
				default:
					break;
				}
				
			}
		};
		
		entities.add(this.player);
		setBackground(Color.WHITE);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/map")));
		
		this.width = Integer.parseInt(reader.readLine());
		this.height = Integer.parseInt(reader.readLine());
		
		this.map = new int[height][width];
		
		String line;
		
		for(int i = 0; i < height; i++) {
			line = reader.readLine();
			for(int j = 0; j < width; j++) {
				this.map[i][j] = Integer.parseInt(line.charAt(j)+"");
			}
		}

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					repaint();
					try {
						Thread.sleep(1000/fps);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public KeyListener getKeyListener() {
		return this.keyListener;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		
		// Updates
		
		for(Entity e : entities) {
			int x = (int)e.getX(), y = (int)e.getY(), width = (int)e.getWidth(), height = (int)e.getHeight();
			Rectangle hitbox = new Rectangle(x, y, width, height);
			
			
			if(e.getVelocityY() < FORCE) {
				e.setVelocityY(e.getVelocityY()+0.2f);
				if(e.getVelocityY() > FORCE)
					e.setVelocityY(FORCE);
			}
						
			
			// Test des collisions
			
			// Vers la gauche
			
			if(isBlock(e.getX()+e.getVelocityX(), e.getY()) ||
			   isBlock(e.getX()+e.getVelocityX(), e.getY()+e.getHeight()-1)) {
				int newX = (int)(e.getX()+e.getVelocityX())/WIDTH+1;
				e.setX(newX*WIDTH);
				e.setVelocityX(0);
			}
			
			// Vers la droite
			
			if(isBlock(e.getX()+e.getWidth()+e.getVelocityX(), e.getY()) ||
					isBlock(e.getX()+e.getWidth()+e.getVelocityX(), e.getY()+e.getHeight()-1)) {
				int newX = (int)(e.getX()+e.getWidth()+e.getVelocityX())/WIDTH-1;
				e.setX(newX*WIDTH-1);
				e.setVelocityX(0);
			}
						
			// Vers le bas
						
			if(isBlock(e.getX(), e.getY()+e.getHeight()+e.getVelocityY()) ||
			   isBlock(e.getX()+e.getWidth(), e.getY()+e.getHeight()+e.getVelocityY())) {
				int newY = (int)(e.getY()+e.getHeight()+e.getVelocityY())/WIDTH-1;
				e.setY(newY*WIDTH);
				e.setVelocityY(0);
				e.canJump();
			}
			
			// Vers le haut
			
			if(isBlock(e.getX(), e.getY()+e.getVelocityY()) ||
			   isBlock(e.getX()+e.getWidth(), e.getY()+e.getVelocityY())) {
				int newY = (int)(e.getY()+e.getVelocityY())/WIDTH+1;
				e.setY(newY*WIDTH);
				e.setVelocityY(0);
			}
			
			
			e.update();
		}
		
		// Update de la camera
		
		this.camera.update();
		
		// Dessin de la map
		
		g.setColor(new Color(0x2196f3));
		
		int startY = (int)(camera.getY()-camera.getHeight()/2)/WIDTH, endY= (int)(camera.getY()+camera.getHeight()/2)/WIDTH;
		int startX = (int)(camera.getX()-camera.getWidth()/2)/WIDTH, endX = (int)(camera.getX()+camera.getWidth()/2)/WIDTH;
		
		for(int i = startY; i < endY+1; i++) {
			for(int j = startX; j < endX+1; j++) {
				try {
					if(this.map[i][j] > 0) {
						g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), WIDTH, WIDTH);
					}
				}catch(Exception e) {
					
				}
			}
		}
		
		// Dessin des entités
		
		g.setColor(new Color(0x673ab7));
				
		for(Entity e : entities) {
			g.fillRect((int)(camera.getWidth()/2+e.getX()-camera.getX()), (int)(camera.getHeight()/2+e.getY()-camera.getY()), (int)e.getWidth(), (int)e.getHeight());
		}
		
	}
	
	public boolean isBlock(float x, float y) {
		try {
			return this.map[(int)(y/WIDTH)][(int)(x/WIDTH)] > 0;
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean isCollision(Rectangle a, Rectangle b) {
		return !((a.x + a.width < b.x) || (a.y + a.height < b.y) || (a.x > b.x + b.width) || (a.y > b.y + b.height));
	}
	
}
