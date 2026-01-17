package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;


public class Camera2D {
	Vector2 position = new Vector2();
	Vector2 targetPosition = new Vector2();
	Vector2 dimensions = new Vector2();
	Vector2 mapSizePixels = new Vector2();
	boolean followingPlayer = true;
	Vector2 edgeToCenter = new Vector2();
	int tileSize = 32;
	Matrix2 startEnd;
	BufferedImage renderedFrame;
	static BufferedImage[] tileSprites = new BufferedImage[128];
	HUD hud = new HUD();
		
	public Camera2D() {}
	
	public Camera2D(Vector2 position, Vector2 dimensions,Vector2 mapSize) {
		this.position.setEqual(position);
		this.dimensions.setEqual(dimensions);
		this.targetPosition.setEqual(position);
		this.edgeToCenter.setEqual(dimensions.multiplyC(0.5));
		this.mapSizePixels.setEqual(mapSize);
		this.renderedFrame  = new BufferedImage((int)dimensions.x, (int)dimensions.y, BufferedImage.TYPE_INT_ARGB);
		loadTiles(tileSprites);
		//graphics = renderedFrame.getGraphics();
	}
	
	public void render(Player player, KeyHandler keyH, Food[][] food, int[][][] map, Enemy[] enemies) {
		Graphics g = renderedFrame.getGraphics();
		followPlayer(player);
	    renderMap(tileSprites,map,g);
	    renderEnemies(enemies,g); 
	    renderPlayer(player,g);
	    renderFood(food,g);
	    renderWeapon(player,g);
	    renderGroundWeapons(g);
	    renderHUD(player,g);
	    g.dispose();
	}

	
	public void renderEnemies(Enemy[] enemies,Graphics g) {

	    for (Enemy e : enemies) {
	        if (e.deathAnimationFinished) continue; // skip dead enemies

	        Vector2 screenPos = getScreenPosition(e.position);
	        g.drawImage(
	            e.getCurrentFrame(),
	            (int)screenPos.x - 16, // center sprite
	            (int)screenPos.y - 16,
	            null
	        );
	    }
	}

	public void followPlayer(Player player) {
	    // desired new position
	    Vector2 target = this.position.add(player.position.sub(this.position).multiplyC(0.1));

	    // clamp X
	    double halfW = this.dimensions.x / 2;
	    if (target.x < halfW) target.x = halfW;
	    if (target.x > this.mapSizePixels.x - halfW) target.x = this.mapSizePixels.x - halfW;

	    // clamp Y
	    double halfH = this.dimensions.y / 2;
	    if (target.y < halfH) target.y = halfH;
	    if (target.y > this.mapSizePixels.y - halfH) target.y = this.mapSizePixels.y - halfH;

	    // apply new position
	    this.position.setEqual(target);
	}
	
	public Vector2 getScreenPosition(Vector2 object) {
		
		Vector2 screenPos = new Vector2();
		Vector2 cameraPos = this.position;
		
		screenPos.setEqual(edgeToCenter.sub(cameraPos.sub(object)));
		
		return screenPos;
	}
	
	public void renderPlayer( Player player, Graphics imageGraphics) {
		
		Vector2 playerScreenPosition = new Vector2();
		playerScreenPosition.setEqual(getScreenPosition(player.position)); //screen position of player
	
		//imageGraphics.setColor(Color.RED);
		//imageGraphics.fillOval((int)playerScreenPosition.x-16,(int)playerScreenPosition.y-16,32,32);
		imageGraphics.drawImage(player.getCurrentFrame(), (int)playerScreenPosition.x-16, (int)playerScreenPosition.y-16, null);

		//System.out.println(player.idle+" "+ player.walking + " "+player.death+" " + player.idleCFPF +" "+ player.walkingCFPF);
	}
	
	public void renderMap( BufferedImage[] tiles, int[][][] map,Graphics g) {


	    Vector2 topLeft = position.sub(edgeToCenter);
	    Vector2 bottomRight = position.add(edgeToCenter);

	    int mapWidth = map.length;
	    int mapHeight = map[0].length;

	    int startX = (int) Math.floor(topLeft.x / tileSize);
	    int startY = (int) Math.floor(topLeft.y / tileSize);
	    int endX   = (int) Math.ceil(bottomRight.x / tileSize);
	    int endY   = (int) Math.ceil(bottomRight.y / tileSize);

	    // CLAMP TO MAP BOUNDS
	    startX = Math.max(0, startX);
	    startY = Math.max(0, startY);
	    endX   = Math.min(mapWidth, endX);
	    endY   = Math.min(mapHeight, endY);

	    Vector2 worldPos = new Vector2();
	    Vector2 screenPos = new Vector2();
	    
	    //System.out.println(endX + " " + endY);
	    ;
	    for (int x = startX; x < endX; x++) {
	        for (int y = startY; y < endY; y++) {

	            int tileIndex = map[x][y][0];
	            if (tileIndex < 0 || tileIndex >= tiles.length)
	                continue;

	            worldPos.setEqual(x * tileSize, y * tileSize);
	            screenPos.setEqual(getScreenPosition(worldPos));

	            g.drawImage(tiles[tileIndex],(int) screenPos.x,(int) screenPos.y,null);
	        }
	    }

	}

	
	public void renderFood(Food[][] food,Graphics g) {
		
		Vector2 topLeft = this.position.sub(this.edgeToCenter);
		Vector2 bottomRight = this.position.add(edgeToCenter);
		
		 int mapWidth = food.length;
		    int mapHeight = food[0].length;

		    int startX = (int) Math.floor(topLeft.x / tileSize);
		    int startY = (int) Math.floor(topLeft.y / tileSize);
		    int endX   = (int) Math.ceil(bottomRight.x / tileSize);
		    int endY   = (int) Math.ceil(bottomRight.y / tileSize);

		    // CLAMP TO MAP BOUNDS
		    startX = Math.max(0, startX);
		    startY = Math.max(0, startY);
		    endX   = Math.min(mapWidth, endX);
		    endY   = Math.min(mapHeight, endY);
		
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

	}
	
	private static void loadTiles(BufferedImage[] images) {
	    try {
	        // Load Tiles folder from resources
	        URL url = Main.class.getClassLoader().getResource("Tiles");
	        if (url == null) {
	          //  System.out.println("Tiles folder not found in resources!");
	            return;
	        }
	        File folder = new File(url.toURI());
	        File[] files = folder.listFiles();
	        if (files == null) {
	          //  System.out.println("No files in Tiles folder!");
	            return;
	        }
	        int index = 0;
	        for (File f : files) {
	            if (f.getName().toLowerCase().endsWith(".png")) {
	                images[index++] = ImageIO.read(f);
	              //  System.out.println("Loaded: " + f.getName());
	                if (index >= images.length) break;
	            }
	        }
	       // System.out.println("Loaded " + index + " tiles.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void renderWeapon(Player player,Graphics g) {

		
		Vector2 weapon = new Vector2();
		
		player.currentWeapon.updateTargetPos(player);
		player.currentWeapon.updatePos();
		
		weapon.setEqual(getScreenPosition(player.currentWeapon.position));
		g.drawImage(player.currentWeapon.sprite,(int)weapon.x-16,(int)weapon.y-16,null);
		

	}
	
	double angle = 0;
	
	public void renderHUD(Player player,Graphics g) {

		angle+=0.05;	
		
		g.setColor(Color.DARK_GRAY);
		g.fillOval(240, 42, 40, 40);
		g.fillOval(490, 42, 40, 40);
		g.fillRect(259, 42, 250, 40);
		
		g.setColor(Color.getHSBColor(230f, 54.5f, 82.75f));
		g.fillOval(240, 40, 40, 40);
		g.fillOval(490, 40, 40, 40);
		g.fillRect(259, 40, 250, 40);
		
		
		
		
		for(int i = 0 ; i < 5 ;i++) {
			
			if(player.foodInv[i]!=null) {
				g.setColor(Color.GRAY.darker());
				g.fillOval(251+i*60,55,32,24);
				
				if(player.selectedSlot == i) {
					g.setColor(Color.GREEN);
					g.fillOval(251+player.selectedSlot*60,55,32,24);
				}
				
				if(3*Math.sin(angle/5-Math.PI*i/5)-2>0) {
					
					g.drawImage(player.foodInv[i].sprite32,251+i*60,(int)(45-(3*Math.sin(angle/5-Math.PI*i/5)-2)*15),null);
				}else {
					g.drawImage(player.foodInv[i].sprite32,251+i*60,45,null);
				}
				}
			}
		
		
		
		
		
		g.setColor(Color.DARK_GRAY);
		g.fillOval(708, 454, 40, 40);
		g.setColor(Color.getHSBColor(230f, 54.5f, 82.75f));
		g.fillOval(708, 452, 40, 40);
		g.drawImage(player.currentWeapon.sprite,712,456, null);
		
		this.hud.draw((java.awt.Graphics2D)g, player);
		
	}
	
	public void renderGroundWeapons(Graphics g) {
		
	}
}