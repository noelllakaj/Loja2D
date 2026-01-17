package main;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	int health=5;
	Vector2 position = new Vector2();
	Vector2 gridPosition = new Vector2();
	Vector2 targetPosition = new Vector2();
	int tileSize;
	int mapX,mapY; // map size
	boolean[][] obstacles;
	Food[] foodInv = new Food[5];  int selectedSlot = -1;
	Weapon currentWeapon = new Weapon(0);
	public int maxHealth = 5;
	int hasMoved=0;
	
	public Player(Vector2 position,int tileSize,int mapX,int mapY,int[][][] map) {
		this.health = 5;
		this.position.setEqual(position);
		this.targetPosition.setEqual(position);
		this.gridPosition.setEqual(new Vector2((int)this.position.x/32,(int)this.position.y/32));
		this.tileSize = tileSize;
		this.mapX = mapX;
		this.mapY = mapY;
		this.obstacles = new boolean[mapX][mapY];
		this.loadAnimations();
		this.getObstacles(map);
		
	}
	
	public int Update(KeyHandler keyH,Food[][] food) {
		
		hasMoved = move(keyH);
		
	    interact(keyH,food);
	    eatingFruits(keyH);
	    lerp();
	    return hasMoved;
	}
	
	private void eatingFruits(KeyHandler keyH) {
	/*	if (keyH.pressed1 && selectedSlot == 0) { selectedSlot = -1; return;}
		if (keyH.pressed2 && selectedSlot == 1) { selectedSlot = -1; return;}
		if (keyH.pressed3 && selectedSlot == 2) { selectedSlot = -1; return;}
		if (keyH.pressed4 && selectedSlot == 3) { selectedSlot = -1; return;}
		if (keyH.pressed5 && selectedSlot == 4) { selectedSlot = -1; return;}*/ //deselecting
		
		if (keyH.pressed1 && foodInv[0] != null) selectedSlot = 0;
		if (keyH.pressed2 && foodInv[1] != null) selectedSlot = 1;
		if (keyH.pressed3 && foodInv[2] != null) selectedSlot = 2;
		if (keyH.pressed4 && foodInv[3] != null) selectedSlot = 3;
		if (keyH.pressed5 && foodInv[4] != null) selectedSlot = 4;
		
		
			
		if(keyH.pressedE && selectedSlot != -1 && maxHealth - health >= foodInv[selectedSlot].regen) {
			health+= foodInv[selectedSlot].regen;
			foodInv[selectedSlot] = null;
			selectedSlot = -1;
		}
		 
	}

	public void loadAnimations() {
		super.idleAnimation = new BufferedImage[4][2];
		super.walkingAnimation = new BufferedImage[4][4];
		super.deathAnimation = new BufferedImage[4][4];
		
		// IDLE ANIMATIONS
		super.idleAnimation = SpriteLoader.load("/Player/Idle","Idle",2);


		// WALKING ANIMATIONS
		super.walkingAnimation = SpriteLoader.load("/Player/Walking", "Walk", 4);
		
		//DEATH ANIMATIONS
		super.deathAnimation = SpriteLoader.load("/Player/Death", "Death", 4);
		
	}
	
	public void takeDamage(int damageTaken) {
		if(damageTaken >= health) {
			health = 0;
			death = true;
		} 
		
		health -= damageTaken;
	}
	

	public void lerp() {

	    Vector2 direction = targetPosition.sub(position);
	    float distance = (float) direction.magnitude();

	    if (distance == 0)
	        return;

	    float speed = 0.5f; // units per frame (CHANGE THIS)

	    if (distance <= speed) {
	        position.setEqual(targetPosition);
	        return;
	    }

	    direction.normalize();
	    position.setEqual(position.add(direction.multiplyC(speed)));
	}
	
	public int move(KeyHandler keyHandler) { //returns 1 if player moves and 0 otherwise
		
		if(health<=0) {
			super.idle=false;
			super.walking = false;
			super.death = true;
			return 0;
		}
		
		boolean temp;
		temp=!(this.targetPosition.sub(this.position).magnitude2()>2);
		
		
		
		super.walking = keyHandler.pressedA 
						|| keyHandler.pressedW 
						|| keyHandler.pressedD
						|| keyHandler.pressedS
						|| !temp;
		
		if(keyHandler.pressedA && this.gridPosition.x>0 && temp) {
			if(!obstacles[(int)this.gridPosition.x-1][(int)this.gridPosition.y]) {
				this.gridPosition.x-=1;
				this.targetPosition.x = this.gridPosition.x*tileSize+15;
				super.idle=false;
				super.direction = directions.left;
				return 1;
			}
		}
		
		if(keyHandler.pressedW && this.gridPosition.y>0 && temp) {
			if(!obstacles[(int)this.gridPosition.x][(int)this.gridPosition.y-1]) {
			this.gridPosition.y-=1;
			this.targetPosition.y = this.gridPosition.y*tileSize+15;//+ tile offset
			super.idle=false;
			super.direction = directions.up;
			return 1;
			}
		}
		
		if(keyHandler.pressedD && this.gridPosition.x<mapX-1 && temp) {//map size -1
			if(!obstacles[(int)this.gridPosition.x+1][(int)this.gridPosition.y]) {
				this.gridPosition.x+=1;
				this.targetPosition.x = this.gridPosition.x*tileSize+15;
				super.idle=false;
				super.direction = directions.right;
				return 1;
			}
		}
		
		if(keyHandler.pressedS && this.gridPosition.y<mapY-1 && temp) {//map size -1
			if(!obstacles[(int)this.gridPosition.x][(int)this.gridPosition.y+1]) {
				this.gridPosition.y+=1;
				this.targetPosition.y = this.gridPosition.y*tileSize+15;
				super.idle=false;
				super.direction = directions.down;
				return 1;
			}
			
		}	
		if(health>0 && !super.walking) {
			super.walking = false;
			super.idle = true;
			return 0;
			}
		return 0;
	}
	
	public void interact(KeyHandler keyH,Food[][] foodMap) {
		//if E is pressed
		if(keyH.pressedE) {
			int gridX = (int)this.gridPosition.x,gridY=(int)this.gridPosition.y;
				if(foodMap[gridX][gridY] != null) {
					for(int i = 0 ; i < this.foodInv.length ; i++) {
						if(this.foodInv[i] == null) {
							this.foodInv[i] = foodMap[gridX][gridY];
							foodMap[gridX][gridY]=null;
							return;
						}
					}
				}
		}
	}
	
	public void getObstacles(int[][][] map) {
		for(int i = 0 ; i < this.mapX ; i++) {
			for(int j = 0 ; j < this.mapY ; j++) {
				if(map[i][j][1] == 1) {
					obstacles[i][j] = true;
				}
			}
		}
	}
}
