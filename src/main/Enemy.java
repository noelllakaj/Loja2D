package main;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity {
	
	public static Enemy[][] enemyArray;
	public static Enemy[] enemyList;
	

    Vector2 position = new Vector2();
    Vector2 gridPosition = new Vector2();
    Vector2 targetPosition = new Vector2();

    static int tileSize = 32;
    static int mapX, mapY;
    static boolean[][] obstacles;

    int damage = 1;
    int attackCooldown = 500;
    int attackTimer = 0;
    
    boolean dead = false;

    public Enemy(Vector2 startPos) {
        
        this.position.setEqual(startPos);
        this.targetPosition.setEqual(startPos);
        this.gridPosition.setEqual(
            new Vector2((int)startPos.x / tileSize, (int)startPos.y / tileSize)
        );

        idle = true;
        
        loadAnimations();
    }
    
    public static void spawnEnemies(int toSpawn,boolean[][] passedObstacles) {
    	
    	obstacles = passedObstacles;
    	
    	mapX = passedObstacles.length;
    	mapY = passedObstacles[0].length;
    	
    	Random random = new Random();
    	int spawnGridX,spawnGridY;
    	
    	enemyList = new Enemy[toSpawn];
    	enemyArray = new Enemy[obstacles.length][obstacles[0].length];
    	
    	for(int i = 0 ; i < toSpawn ; i++) {
    		for(;;) {
    			spawnGridX = random.nextInt(30);
        		spawnGridY = random.nextInt(30);
        		
        		if(obstacles[spawnGridX][spawnGridY] == false 
        		  && enemyArray[spawnGridX][spawnGridY] == null) {
        			Vector2 startPos = new Vector2(spawnGridX,spawnGridY).multiplyC(tileSize).add(new Vector2(15,15));        			
        			enemyList[i] = new Enemy(startPos);
        			enemyArray[spawnGridX][spawnGridY] = enemyList[i];	
        			enemyList[i].damage = random.nextInt(4)+1;
        			break;
        		}
    		}
    	}
    }

    public static void update(Player player) {
    	if(playerMoved)
    		moveTowardsPlayer(player);
        for(Enemy e : enemyList) {
        	if(e.death==false) {
        		
        		e.lerp();
        		e.attack(player);
        	}
        }
        if(playerMoved) playerMoved = false;
    }

    public static void moveTowardsPlayer(Player player) {
    	for(Enemy enemy : enemyList) {
        if (!enemy.isAtTarget()) return;

        int dx = (int)(Player.gridPosition.x - enemy.gridPosition.x);
        int dy = (int)(Player.gridPosition.y - enemy.gridPosition.y);

        if (Math.abs(dx) > Math.abs(dy)) {
            if (enemy.tryMove(enemy.sign(dx), 0) == 0)
            	enemy.tryMove(0, enemy.sign(dy));
        } else {
            if (enemy.tryMove(0, enemy.sign(dy)) == 0)
            	enemy.tryMove(enemy.sign(dx), 0);
        }
    }
    }

    private int sign(int v) {
        return Integer.compare(v, 0);
    }

    private boolean isAtTarget() {
        return targetPosition.sub(position).magnitude2() <= 2;
    }


    private int tryMove(int xDir, int yDir) {

        int nx = (int)gridPosition.x + xDir;
        int ny = (int)gridPosition.y + yDir;

        if (nx < 0 || ny < 0 || nx >= mapX || ny >= mapY)
            return 0;

        if (obstacles[nx][ny] && enemyArray[nx][ny]==null) {
        	super.idle = true;
        	super.walking = false;
        	return 0;
        } 
        super.walking = true;

        gridPosition.x = nx;
        gridPosition.y = ny;

        targetPosition.x = gridPosition.x * tileSize + 15;
        targetPosition.y = gridPosition.y * tileSize + 15;

        updateDirection(xDir, yDir);
        return 1;
    }

    private void updateDirection(int xDir, int yDir) {
        if (xDir == -1) direction = directions.left;
        if (xDir == 1)  direction = directions.right;
        if (yDir == -1) direction = directions.up;
        if (yDir == 1)  direction = directions.down;
    }

    public void lerp() {
        Vector2 dir = targetPosition.sub(position);
        float dist = (float) dir.magnitude();
        if (dist == 0) return;

        float speed = 0.4f;

        if (dist <= speed) {
            position.setEqual(targetPosition);
            return;
        }

        dir.normalize();
        position.setEqual(position.add(dir.multiplyC(speed)));
    }

    public void attack(Player player) {

    	boolean attackTry =
    		    (int)gridPosition.x == (int)Player.gridPosition.x &&
    		    (int)gridPosition.y == (int)Player.gridPosition.y;

    	
        if (!attackTry)
            return;

        if (attackTimer > 0) {
            attackTimer--;
            return;
        }
        if(damage <= Player.currentWeapon.damage) {
       	super.death = true;
        	return;
        }

        player.takeDamage(damage);
        attackTimer = attackCooldown;
    }

    public static void generateEnemies(int[][][] map) {
    	enemyArray = new Enemy[map.length][map[0].length];
    }

    public void loadAnimations() {
		super.idleAnimation = new BufferedImage[4][5];
		super.walkingAnimation = new BufferedImage[4][10];
		super.deathAnimation = new BufferedImage[4][7];
		// IDLE ANIMATIONS
		super.idleAnimation = SpriteLoader.load("/Zombie/zombieIdle","Idle",5);

		// WALKING ANIMATIONS
		super.walkingAnimation = SpriteLoader.load("/Zombie/zombieWalking","Walk",10);
		
		//DEATH ANIMATIONS
		super.deathAnimation = SpriteLoader.load("/Zombie/zombieDeath", "Death", 7);
		
	}
    
    public String toString() {
    	return "pos : " + this.gridPosition.x + " " + this.gridPosition.y + " " +this.damage;
    			
    }
    
	
}
