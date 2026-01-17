package main;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    Vector2 position = new Vector2();
    Vector2 gridPosition = new Vector2();
    Vector2 targetPosition = new Vector2();

    int tileSize;
    int mapX, mapY;
    boolean[][] obstacles;

    int damage = 1;
    int attackCooldown = 300;
    int attackTimer = 0;
    
    boolean dead = false;

    public Enemy(Vector2 startPos, int tileSize, int mapX, int mapY, boolean[][] obstacles) {
        this.tileSize = tileSize;
        this.mapX = mapX;
        this.mapY = mapY;
        this.obstacles = obstacles;

        this.position.setEqual(startPos);
        this.targetPosition.setEqual(startPos);
        this.gridPosition.setEqual(
            new Vector2((int)startPos.x / tileSize, (int)startPos.y / tileSize)
        );

        idle = true;
        loadAnimations();
    }

    public void update(Player player) {
        if (death) return;

      //  moveTowardsPlayer(player);
        lerp();
        attack(player);
    }

    public void moveTowardsPlayer(Player player) {

        if (!isAtTarget()) return;

        int dx = (int)(player.gridPosition.x - gridPosition.x);
        int dy = (int)(player.gridPosition.y - gridPosition.y);

        if (Math.abs(dx) > Math.abs(dy)) {
            if (tryMove(sign(dx), 0) == 0)
                tryMove(0, sign(dy));
        } else {
            if (tryMove(0, sign(dy)) == 0)
                tryMove(sign(dx), 0);
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

        if (obstacles[nx][ny]) {
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
    		    (int)gridPosition.x == (int)player.gridPosition.x &&
    		    (int)gridPosition.y == (int)player.gridPosition.y;

    	
        if (!attackTry)
            return;

        if (attackTimer > 0) {
            attackTimer--;
            return;
        }
        if(damage <= player.currentWeapon.damage) {
       	super.death = true;
        	return;
        }

        player.takeDamage(damage);
        attackTimer = attackCooldown;
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
	
}
