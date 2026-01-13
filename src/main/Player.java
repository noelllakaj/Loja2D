package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity{
	int health=5;
	Vector2 position = new Vector2();
	Vector2 gridPosition = new Vector2();
	Vector2 targetPosition = new Vector2();
	int tileSize;
	int mapX,mapY; // map size
	boolean[][] obstacles;
	Food[] foodInv = new Food[5];
	
	public Player(Vector2 position,int tileSize,int mapX,int mapY) {
		this.health = 3;
		this.position.setEqual(position);
		this.targetPosition.setEqual(position);
		this.gridPosition.setEqual(new Vector2((int)this.position.x/32,(int)this.position.y/32));
		this.tileSize = tileSize;
		this.mapX = mapX;
		this.mapY = mapY;
		this.obstacles = new boolean[mapX][mapY];
	}
	
	public void loadAnimations() {
		super.idleAnimation = new BufferedImage[4][2];
		super.walkingAnimation = new BufferedImage[4][4];
		super.deathAnimation = new BufferedImage[4][4];
		try {
			super.idleAnimation[0][0] =  load("/Player/Idle/upIdle1.png");
			super.idleAnimation[0][1] =  load("/Player/Idle/upIdle2.png");
			super.idleAnimation[1][0] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/leftIdle1.png"));
			super.idleAnimation[1][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/leftIdle2.png"));
			super.idleAnimation[2][0] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/downIdle1.png"));
			super.idleAnimation[2][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/downIdle2.png"));
			super.idleAnimation[3][0] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/rightIdle1.png"));
			super.idleAnimation[3][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Idle/rightIdle2.png"));
			
			super.walkingAnimation[0][0] =  load("/Player/Walking/upWalk (1).png");
			super.walkingAnimation[0][1] =  load("/Player/Walking/upWalk (2).png");
			super.walkingAnimation[0][2] =  load("/Player/Walking/upWalk (3).png");
			super.walkingAnimation[0][3] =  load("/Player/Walking/upWalk (4).png");
			super.walkingAnimation[1][0] =  load("/Player/Walking/leftWalk (1).png");
			super.walkingAnimation[1][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/leftWalk (2).png"));
			super.walkingAnimation[1][2] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/leftWalk (3).png"));
			super.walkingAnimation[1][3] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/leftWalk (4).png"));
			super.walkingAnimation[2][0] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/downWalk (1).png"));
			super.walkingAnimation[2][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/downWalk (2).png"));
			super.walkingAnimation[2][2] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/downWalk (3).png"));
			super.walkingAnimation[2][3] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/downWalk (4).png"));
			super.walkingAnimation[3][0] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/rightWalk (1).png"));
			super.walkingAnimation[3][1] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/rightWalk (2).png"));
			super.walkingAnimation[3][2] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/rightWalk (3).png"));
			super.walkingAnimation[3][3] =  ImageIO.read(getClass().getResourceAsStream("/Player/Walking/rightWalk (4).png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private BufferedImage load(String path) throws IOException {
	    var stream = getClass().getResourceAsStream(path);
	    if (stream == null) {
	        throw new RuntimeException("Missing resource: " + path);
	    }
	    return ImageIO.read(stream);
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

	
	public void move(KeyHandler keyHandler) {
		
		if(health<=0) {
			super.idle=false;
			super.walking = false;
			super.death = true;
			return;
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
				super.direction = 1;
				return;
			}
		}
		
		if(keyHandler.pressedW && this.gridPosition.y>0 && temp) {
			if(!obstacles[(int)this.gridPosition.x][(int)this.gridPosition.y-1]) {
			this.gridPosition.y-=1;
			this.targetPosition.y = this.gridPosition.y*tileSize+15;//+ tile offset
			super.idle=false;
			super.direction = 0;
			return;
			}
		}
		
		if(keyHandler.pressedD && this.gridPosition.x<mapX-1 && temp) {//map size -1
			if(!obstacles[(int)this.gridPosition.x+1][(int)this.gridPosition.y]) {
				this.gridPosition.x+=1;
				this.targetPosition.x = this.gridPosition.x*tileSize+15;
				super.idle=false;
				super.direction = 3;
				return;
			}
		}
		
		if(keyHandler.pressedS && this.gridPosition.y<mapY-1 && temp) {//map size -1
			if(!obstacles[(int)this.gridPosition.x][(int)this.gridPosition.y+1]) {
				this.gridPosition.y+=1;
				this.targetPosition.y = this.gridPosition.y*tileSize+15;
				super.idle=false;
				super.direction = 2;
				return;
			}
			
		}	
		if(health>0 && !super.walking) {
			super.walking = false;
			super.idle = true;
			return;
			}
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
