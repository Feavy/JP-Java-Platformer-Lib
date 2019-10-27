# JP Java Platformer Lib (*WIP*)
## A propos
JP Java Platformer Lib est une bibliothèque Java permettant de créer des jeux de plateformes en 2D.

Un exemple d'implémentation peut être retrouvé ici : [https://github.com/Feavy/JP-Java-Platformer](https://github.com/Feavy/JP-Java-Platformer).
## Roadmap :
- Définir la couleur des SimpleEntity.
- Supporter plus de 10 types de tuiles.
- Sauvegarde des entités dans les fichiers de map.

## Documentation
### Tuiles :
Il est possible de redéfinir la classe **Tile** pour créer des tuiles personnalisées.

La classe **SimpleTile** fournie une implémentation basique d'une tuile ayant simplement une couleur.

**Exemple** (*création d'une tuile monochrome ayant un comportement particulier au contact d'une entité*) :
```java
Tile tile = new SimpleTile(new Color(0xF34F52)) {  
    @Override  
    public void onCollision(Entity e, Side side) {  
        if (side == Side.TOP && !e.isFrozen()) {  
            e.freeze();  
            System.err.println("Vous êtes gelé !");  
        }  
    }
};
```

### Chargement d'une map :
#### Structure d'un fichier map :
```
[hauteur]
[largeur]
[chiffre][chiffre][chiffre]...[chiffre]
...
[chiffre][chiffre][chiffre]...[chiffre]
```
**Exemple :**
```
3
2
001
011
111
```
#### Association des entiers à des tuiles personnalisées :
Création d'un tableau de tuile :
```java
Tile[] tiles = new Tile[]{new SimpleTile(new Color(0x2196f3)), new SimpleTile(new Color(0xF34F52)) {  
    @Override  
    public void onCollision(Entity e, Side side) {  
        if (side == Side.TOP && !e.isFrozen()) {  
            e.freeze();  
			System.err.println("Vous êtes gelé !");  
        }  
    }  
}}
```
Ce tableau attribue les index à leurs tuiles.
#### Création de la map :
```java
Map map = Map.fromFile("/map",	// Le fichier contenant les données de la map
					   tiles	// Les tuiles à utiliser
		  );

```
### Entités :
Il est possible de redéfinir la classe **Entity** pour créer des entités personnalisées.

La classe **SimpleEntity** fournie une implémentation basique d'une entité.

**Exemple** (*création d'une entité monochrome se déplaçant aléatoirement*) :
```java
Entity entity = new SimpleEntity(256, 100, 28, 32) {
    private int count = 0;
    private Random rand = new Random();

    @Override
    public void onCollision(Entity other, Side side) {}

    @Override
    public void update() {
        super.update();
        count++;
        if (count >= 10) {
            count = 0;
            int r = rand.nextInt(2);
            int r2 = rand.nextInt(2);
            if (r2 == 0)
                jump();
            if (r == 0) {
                startMoving(Direction.LEFT);
            } else {
                startMoving(Direction.RIGHT);
            }
        }
    }
}
```
### Caméra :
La caméra définit le partie de la map qui est affichée. Elle suit forcément une entité.

**Exemple** (*création d'une caméra suivant l'entité précédemment créée*) :

```java
Camera camera  = new Camera(entity, 800, 800);
```
### Création du panel de jeu :
```java
JavaPlatformer game = new JavaPlatformer(player, camera, map);

// Association de ce panel à une JFrame :
frame.setContentPane(game);
```
