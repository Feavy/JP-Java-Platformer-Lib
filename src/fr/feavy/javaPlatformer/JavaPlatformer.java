package fr.feavy.javaPlatformer;

import fr.feavy.javaPlatformer.map.Map;
import fr.feavy.javaPlatformer.map.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JavaPlatformer extends JPanel {
    private Player player;
    private Camera camera;

    private List<Entity> entities = new ArrayList<>();

    private Map map;
    public int tileWidth;

    private KeyListener keyListener;

    private final float FORCE = 9.8f;

    public JavaPlatformer(int tileWidth) throws NumberFormatException, IOException {
        setBackground(Color.WHITE);
        this.tileWidth = tileWidth;

        // Création de KeyListener gérant les déplacements du joueur

        this.keyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (player == null)
                    return;

                switch (e.getKeyCode()) {
                    case 37:    // Gauche
                        player.stopMoving();
                        break;
                    case 38:    // Haut
                        player.stopJumping();
                        break;
                    case 39:    // Droite
                        player.stopMoving();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (player == null)
                    return;

                switch (e.getKeyCode()) {
                    case 37:    // Gauche
                        player.startMoving(Direction.LEFT);
                        break;
                    case 38:    // Haut
                        player.startJumping();
                        break;
                    case 39:    // Droite
                        player.startMoving(Direction.RIGHT);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    public void start() throws Exception {
        if (player == null)
            throw new Exception("Erreur : aucun joueur n'a été défini.");
        if (camera == null)
            throw new Exception("Erreur : aucune caméra n'a été définie.");
        if (map == null)
            throw new Exception("Erreur : aucune map n'a été définie.");

        try {
            ((JFrame) SwingUtilities.getWindowAncestor(this)).addKeyListener(this.keyListener);
        } catch (NullPointerException e) {
            throw new Exception("Erreur : le jeu n'est dans aucune frame.");
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return this.map;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setPlayer(Player player) {
        this.player = player;

        entities.add(player);
    }

    public Player getPlayer() {
        return player;
    }

    public void addEntity(Entity e) {
        this.entities.add(e);
    }

    public void removeEntity(Entity e) {
        this.entities.remove(e);
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

        Graphics2D g = (Graphics2D) g1;

        // Updates

        for (Entity e : entities) {

            // Gravité

            if (e.getVelocityY() < FORCE) {
                e.setVelocityY(e.getVelocityY() + 0.2f);
                if (e.getVelocityY() > FORCE)
                    e.setVelocityY(FORCE);
            }


            // Test des collisions avec la map

            // Vers la gauche

            Optional<Tile> t;

            if ((t = map.getTile(e.getX() + e.getVelocityX(), e.getY(), tileWidth)).isPresent()) {
                int newX = (int) (e.getX() + e.getVelocityX()) / tileWidth + 1;
                e.setX(newX * tileWidth);
                e.setVelocityX(0);
                t.get().onCollision(e, Side.RIGHT);
            }

            if ((t = map.getTile(e.getX() + e.getVelocityX(), e.getY() + e.getHeight() - 1, tileWidth)).isPresent()){
                int newX = (int) (e.getX() + e.getVelocityX()) / tileWidth + 1;
                e.setX(newX * tileWidth);
                e.setVelocityX(0);
                t.get().onCollision(e, Side.RIGHT);
            }

            // Vers la droite

            if ((t = map.getTile(e.getX() + e.getWidth() + e.getVelocityX(), e.getY(), tileWidth)).isPresent()) {
                int newX = (int) (e.getX() + e.getWidth() + e.getVelocityX()) / tileWidth - 1;
                e.setX(newX * tileWidth + 3);
                e.setVelocityX(0);
                t.get().onCollision(e, Side.LEFT);
            }

            if ((t = map.getTile(e.getX() + e.getWidth() + e.getVelocityX(), e.getY() + e.getHeight() - 1, tileWidth)).isPresent()) {
                int newX = (int) (e.getX() + e.getWidth() + e.getVelocityX()) / tileWidth - 1;
                e.setX(newX * tileWidth + 3);
                e.setVelocityX(0);
                t.get().onCollision(e, Side.LEFT);
            }

            // Vers le bas

            if ((t = map.getTile(e.getX(), e.getY() + e.getHeight() + e.getVelocityY(), tileWidth)).isPresent()) {
                int newY = (int) (e.getY() + e.getHeight() + e.getVelocityY()) / tileWidth - 1;
                e.setY(newY * tileWidth);
                e.setVelocityY(0);
                e.canJump();
                t.get().onCollision(e, Side.TOP);
            }

            if ((t = map.getTile(e.getX() + e.getWidth(), e.getY() + e.getHeight() + e.getVelocityY(), tileWidth)).isPresent()) {
                int newY = (int) (e.getY() + e.getHeight() + e.getVelocityY()) / tileWidth - 1;
                e.setY(newY * tileWidth);
                e.setVelocityY(0);
                e.canJump();
                t.get().onCollision(e, Side.TOP);
            }

            // Vers le haut

            if ((t = map.getTile(e.getX(), e.getY() + e.getVelocityY(), tileWidth)).isPresent()) {
                int newY = (int) (e.getY() + e.getVelocityY()) / tileWidth + 1;
                e.setY(newY * tileWidth);
                e.setVelocityY(0);

                t.get().onCollision(e, Side.BOTTOM);
            }

            if ((t = map.getTile(e.getX() + e.getWidth(), e.getY() + e.getVelocityY(), tileWidth)).isPresent()) {
                int newY = (int) (e.getY() + e.getVelocityY()) / tileWidth + 1;
                e.setY(newY * tileWidth);
                e.setVelocityY(0);

                t.get().onCollision(e, Side.BOTTOM);
            }

            // Collisions avec autres entités

            for (Entity e2 : entities) {
                if (e != e2) {
                    Rectangle hitbox = new Rectangle((int) (e.getX() + e.getVelocityX()), (int) (e.getY() + 1), (int) e.getWidth(), (int) (e.getHeight() - 2));
                    Rectangle hitbox2 = new Rectangle((int) e2.getX(), (int) (e2.getY() + 1), (int) e2.getWidth(), (int) (e2.getHeight() - 2));

                    if (isCollision(hitbox, hitbox2)) {
                        if (e.getVelocityX() > 0 && e.getX() < e2.getX()) {    // Vers la droite
                            e.setX(e2.getX() - e.getWidth());
                            e.setVelocityX(0);

                            e.onCollision(e2, Side.RIGHT);
                            e2.onCollision(e, Side.LEFT);
                        } else if (e.getVelocityX() < 0 && e.getX() > e2.getX()) {    // Vers la gauche
                            e.setX(e2.getX() + e2.getWidth());
                            e.setVelocityX(0);

                            e.onCollision(e2, Side.LEFT);
                            e2.onCollision(e, Side.RIGHT);
                        }
                    }

                    hitbox = new Rectangle((int) (e.getX() + 1), (int) (e.getY() + e.getVelocityY()), (int) (e.getWidth() - 2), (int) (e.getHeight()));
                    hitbox2 = new Rectangle((int) (e2.getX() + 1), (int) (e2.getY()), (int) (e2.getWidth() - 2), (int) (e2.getHeight()));

                    if (isCollision(hitbox, hitbox2)) {
                        // Verification e2.y > e.y pour éviter les fausses détection lors de la chute après collision
                        if (e.getVelocityY() > 0 && e2.getY() > e.getY()) {    // Vers le bas
                            e.setY(e2.getY() - e.getHeight());
                            e.canJump();
                            e.setVelocityY(0);

                            e.onCollision(e2, Side.BOTTOM);
                            e2.onCollision(e2, Side.TOP);
                        } else if (e.getVelocityY() < 0) {    // Vers le haut
                            e.setY(e2.getY() + e2.getHeight());
                            e2.canJump();
                            e.setVelocityY(0);

                            e.onCollision(e2, Side.TOP);
                            e2.onCollision(e2, Side.BOTTOM);
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

        int startY = (int) (camera.getY() - camera.getHeight() / 2) / tileWidth, endY = (int) (camera.getY() + camera.getHeight() / 2) / tileWidth;
        int startX = (int) (camera.getX() - camera.getWidth() / 2) / tileWidth, endX = (int) (camera.getX() + camera.getWidth() / 2) / tileWidth;

        for (int i = startY; i < endY + 1; i++) {
            for (int j = startX; j < endX + 1; j++) {
                try {
                    if (this.map.getTile(j, i).isPresent()) {
                        Tile t = this.map.getTile(j, i).get();
                        g.setColor(t.getColor());
                        g.fillRoundRect((int) (camera.getWidth() / 2 + j * tileWidth - camera.getX()), (int) (camera.getHeight() / 2 + i * tileWidth - camera.getY()), tileWidth, tileWidth, 20, 20);
                        if (i - 1 >= 0 && this.map.getTile(j, i - 1).isPresent()) {
                            g.fillRect((int) (camera.getWidth() / 2 + j * tileWidth - camera.getX()), (int) (camera.getHeight() / 2 + i * tileWidth - camera.getY()), tileWidth, 20);
                        }
                        if (i + 1 < this.map.getHeight() && this.map.getTile(j, i + 1).isPresent()) {
                            g.fillRect((int) (camera.getWidth() / 2 + j * tileWidth - camera.getX()), (int) (camera.getHeight() / 2 + i * tileWidth - camera.getY() + tileWidth - 20), tileWidth, 20);
                        }
                        if (j - 1 >= 0 && this.map.getTile(j - 1, i).isPresent()) {
                            g.fillRect((int) (camera.getWidth() / 2 + j * tileWidth - camera.getX()), (int) (camera.getHeight() / 2 + i * tileWidth - camera.getY()), 20, tileWidth);
                        }
                        if (j + 1 < this.map.getWidth() && this.map.getTile(j + 1, i).isPresent()) {
                            g.fillRect((int) (camera.getWidth() / 2 + j * tileWidth - camera.getX() + tileWidth - 20), (int) (camera.getHeight() / 2 + i * tileWidth - camera.getY()), 20, tileWidth);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        // Dessin des entités

        g.setColor(new Color(0x673ab7));

        for (Entity e : entities) {
            g.fillRoundRect((int) (camera.getWidth() / 2 + e.getX() - camera.getX()), (int) (camera.getHeight() / 2 + e.getY() - camera.getY()), (int) e.getWidth(), (int) e.getHeight(), 20, 20);
        }

    }
}
