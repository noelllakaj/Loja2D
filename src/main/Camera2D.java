package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Camera2D {
	Vector2 position = new Vector2();
	Vector2 targetPosition = new Vector2();
	Vector2 dimensions = new Vector2();
	Vector2 mapSize = new Vector2();
	boolean followingPlayer = true;
	Vector2 edgeToCenter = new Vector2();
	int tileSize = 32;
	Matrix2 startEnd;
		
	public Camera2D() {}
	
	public Camera2D(Vector2 position, Vector2 dimensions,Vector2 mapSize) {
		this.position.setEqual(position);
		this.dimensions.setEqual(dimensions);
		this.targetPosition.setEqual(position);
		this.edgeToCenter.setEqual(dimensions.multiplyC(0.5));
		this.mapSize.setEqual(mapSize);

	}
	
	public void followPlayer(Player player) {
	    // desired new position
	    Vector2 target = this.position.add(player.position.sub(this.position).multiplyC(0.1));

	    // clamp X
	    double halfW = this.dimensions.x / 2;
	    if (target.x < halfW) target.x = halfW;
	    if (target.x > this.mapSize.x - halfW) target.x = this.mapSize.x - halfW;

	    // clamp Y
	    double halfH = this.dimensions.y / 2;
	    if (target.y < halfH) target.y = halfH;
	    if (target.y > this.mapSize.y - halfH) target.y = this.mapSize.y - halfH;

	    // apply new position
	    this.position.setEqual(target);
	}
	
	public Vector2 getScreenPosition(Vector2 object) {
		
		Vector2 screenPos = new Vector2();
		Vector2 cameraPos = this.position;
		
		screenPos.setEqual(edgeToCenter.sub(cameraPos.sub(object)));
		
		return screenPos;
	}
	
	public void renderPlayer(BufferedImage image, Player player) {
		
		Vector2 playerScreenPosition = new Vector2();
		playerScreenPosition.setEqual(getScreenPosition(player.position)); //screen position of player
		
		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.RED);
		//imageGraphics.fillOval((int)playerScreenPosition.x-16,(int)playerScreenPosition.y-16,32,32);
		imageGraphics.drawImage(player.getCurrentFrame(), (int)playerScreenPosition.x-16, (int)playerScreenPosition.y-16, null);

		imageGraphics.dispose();
		//System.out.println(player.idle+" "+ player.walking + " "+player.death+" " + player.idleCFPF +" "+ player.walkingCFPF);
	}
	
	public void drawLine(BufferedImage image) {
		Vector2 startPoint = new Vector2(400,0);
		Vector2 endPoint = new Vector2(400,600);
		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.BLACK);
		imageGraphics.drawLine((int)getScreenPosition(startPoint).x, (int)getScreenPosition(startPoint).y,
							   (int)getScreenPosition(endPoint).x, (int)getScreenPosition(endPoint).y);
		imageGraphics.dispose();
		
	}
	
	public void drawMap(BufferedImage image,int[][][] map,int length) { //optimize
		Graphics g = image.getGraphics();
		Vector2 firstPoint = new Vector2();
		Vector2 sfp = new Vector2();
		
		for(int i = 0 ; i<30;i++) {
			for(int j = 0 ; j < 30 ; j++) {
				if(map[i][j][1]!=0) {
				Color color = new Color(map[i][j][0]*12+17,map[i][j][0]*19+35,map[i][j][0]*5+156);
				g.setColor(color);
				} else {
					g.setColor(Color.BLACK);
				}
				
				firstPoint.setEqual(new Vector2(i*length,j*length));
				sfp.setEqual(getScreenPosition(firstPoint));
				g.fillRect((int)sfp.x,(int)sfp.y,length,length);
			}
		}
	}
	public void renderMap(BufferedImage image,BufferedImage[] tiles, int[][][] map) {
		
		Vector2 topLeft = this.position.sub(this.edgeToCenter);
		Vector2 bottomRight = this.position.add(edgeToCenter);
		
		int startX = (int)topLeft.x/this.tileSize;
		int startY = (int)topLeft.y/this.tileSize;
		int endX = (int) bottomRight.x/this.tileSize+1;
		int endY = (int) bottomRight.y/this.tileSize+1;
		
		if(endX>(int)this.mapSize.x/this.tileSize) 
			endX-=2;
		if(endY>(int)this.mapSize.y/this.tileSize) 
			endY-=2;
		
		
		Graphics g = image.getGraphics();
		Vector2 firstPoint = new Vector2();
		Vector2 sfp = new Vector2();
		for(int i = startX ; i <endX ; i++) {
			for(int j = startY ; j < endY ; j++) {
				firstPoint.setEqual(new Vector2(i*32,j*32));
				sfp.setEqual(getScreenPosition(firstPoint));
				g.drawImage(tiles[map[i][j][0]],(int)sfp.x,(int)sfp.y,null);
			}
		}
		g.dispose();
	}
	
	public void renderFood(BufferedImage image,Food[][] food) {
		
		Vector2 topLeft = this.position.sub(this.edgeToCenter);
		Vector2 bottomRight = this.position.add(edgeToCenter);
		
		int startX = (int)topLeft.x/this.tileSize;
		int startY = (int)topLeft.y/this.tileSize;
		int endX = (int) bottomRight.x/this.tileSize+1;
		int endY = (int) bottomRight.y/this.tileSize+1;
		
		if(endX>(int)this.mapSize.x/this.tileSize) 
			endX-=2;
		if(endY>(int)this.mapSize.y/this.tileSize) 
			endY-=2;
		
		Graphics g = image.getGraphics();
		Vector2 firstPoint = new Vector2();
		Vector2 sfp = new Vector2();
		for(int i = startX ; i < endX ; i++) {
			for(int j = startY ; j < endY ; j++) {
				firstPoint.setEqual(new Vector2(i*this.tileSize,j*this.tileSize));
				sfp.setEqual(getScreenPosition(firstPoint));
				if(food[i][j]!=null)
				g.drawImage(food[i][j].sprite,(int)sfp.x+8,(int)sfp.y+8,null);
			}
		}
		g.dispose();
	}
}