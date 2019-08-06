package fr.feavy.javaPlatformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		setBackground(Color.WHITE);
		
		this.player = new Player(0, 0);
		this.camera = new Camera(player, 800, 800);
		
		// Création de KeyListener gérant les déplacements du joueur
		
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
		
		for(int i = 0; i < 3; i++)
		entities.add(new Entity(256, 100, 28, 32) {
			
			private int count = 0;
			private Random rand = new Random();
			
			@Override
			public void onCollision(Collidable other) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void update() {
				super.update();
				count++;
				if(count >= 10) {
					count = 0;
					int r = rand.nextInt(2);
					int r2 = rand.nextInt(2);
					if(r2 == 0)
						jump();
					if(r == 0) {
						startMoving(Direction.LEFT);
					}else {
						startMoving(Direction.RIGHT);
					}
				}
			}
		});
		
		
		getMapFromFile("/map");
		
		this.player.setY((this.height-2)*32);
		
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
	
	private void getMapFromFile(String path) throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

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

	}
	
	public KeyListener getKeyListener() {
		return this.keyListener;
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
	
	/**
	 * Loop d'update
	 */
	public void paint(Graphics g1) {
		// TODO Auto-generated method stub
		super.paint(g1);
		
		Graphics2D g = (Graphics2D)g1;
		
		// Updates
		
		for(Entity e : entities) {		
			
			// Gravité
			
			if(e.getVelocityY() < FORCE) {
				e.setVelocityY(e.getVelocityY()+0.2f);
				if(e.getVelocityY() > FORCE)
					e.setVelocityY(FORCE);
			}
						
			
			// Test des collisions avec la map
			
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
				e.setX(newX*WIDTH+3);
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
			
			// Collisions avec autres entités
			
			for(Entity e2 : entities) {
				if(e != e2) {
					Rectangle hitbox = new Rectangle((int)(e.getX()+e.getVelocityX()), (int)(e.getY()+1), (int)e.getWidth(), (int)(e.getHeight()-2));
					Rectangle hitbox2 = new Rectangle((int)e2.getX(), (int)(e2.getY()+1), (int)e2.getWidth(), (int)(e2.getHeight()-2));
					
					if(isCollision(hitbox, hitbox2)) {
						if(e.getVelocityX() > 0 && e.getX() < e2.getX()) {	// Vers la droite
							e.setX(e2.getX()-e.getWidth());
							e.setVelocityX(0);
						}else if(e.getVelocityX() < 0 && e.getX() > e2.getX()){	// Vers la gauche
							e.setX(e2.getX()+e2.getWidth());
							e.setVelocityX(0);
						}
					}
					
					hitbox = new Rectangle((int)(e.getX()+1), (int)(e.getY()+e.getVelocityY()), (int)(e.getWidth()-2), (int)(e.getHeight()));
					hitbox2 = new Rectangle((int)(e2.getX()+1), (int)(e2.getY()), (int)(e2.getWidth()-2), (int)(e2.getHeight()));
					
					if(isCollision(hitbox, hitbox2)) {
						// Verification e2.y > e.y pour éviter les fausses détection lors de la chute après collision
						if(e.getVelocityY() > 0 && e2.getY() > e.getY()) {	// Vers le bas
							e.setY(e2.getY()-e.getHeight());
							e.canJump();
							e.setVelocityY(0);
						}else if(e.getVelocityY() < 0){	// Vers le haut
							e.setY(e2.getY()+e2.getHeight());
							e2.canJump();
							e.setVelocityY(0);
						}
					}
				}
			}
			
			e.update();
		}
		
		// Update de la camera
		
		this.camera.update();
		
		// dessins
		
		// Dessin de la map
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0x2196f3));
		
		int startY = (int)(camera.getY()-camera.getHeight()/2)/WIDTH, endY= (int)(camera.getY()+camera.getHeight()/2)/WIDTH;
		int startX = (int)(camera.getX()-camera.getWidth()/2)/WIDTH, endX = (int)(camera.getX()+camera.getWidth()/2)/WIDTH;
		
		for(int i = startY; i < endY+1; i++) {
			for(int j = startX; j < endX+1; j++) {
				try {
					if(this.map[i][j] > 0) {
						g.fillRoundRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), WIDTH, WIDTH, 20, 20);
						if(i-1 >= 0 &&this.map[i-1][j] > 0) {
							g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), WIDTH, 20);
						}
						if(i+1 < height && this.map[i+1][j] > 0) {
							g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()+WIDTH-20), WIDTH, 20);
						}
						if(j-1 >= 0 && this.map[i][j-1] > 0) {
							g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), 20, WIDTH);
						}
						if(j+1 < width && this.map[i][j+1] > 0) {
							g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()+WIDTH-20), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), 20, WIDTH);
						}
						//g.fillRect((int)(camera.getWidth()/2+j*WIDTH-camera.getX()), (int)(camera.getHeight()/2+i*WIDTH-camera.getY()), WIDTH, WIDTH);
					}
				}catch(Exception e) {
					
				}
			}
		}
		
		// Dessin des entités
		
		g.setColor(new Color(0x673ab7));
				
		for(Entity e : entities) {
			g.fillRoundRect((int)(camera.getWidth()/2+e.getX()-camera.getX()), (int)(camera.getHeight()/2+e.getY()-camera.getY()), (int)e.getWidth(), (int)e.getHeight(), 20, 20);
		}
		
	}
	
}
