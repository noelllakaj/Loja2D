package main;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import javax.swing.JFrame;

public class Main {
	static int tileSize = 32;
	static int mapX=30,mapY=30;
	static Vector2 mapSize = new Vector2(mapX,mapY);
	static int width = tileSize*24, height = tileSize*16; //start frame dimensions 32*24=768 32*16=512
	static Vector2 screenDimensions = new Vector2(width,height);
	static Vector2 mapSizePixels = new Vector2(tileSize*mapX,tileSize*mapY);
	static Food[][] food = new Food[mapX][mapY];
	


	public static void main(String[] args) {
		
		int[][][] map = new int[mapX][mapY][2];	
		loadMapFromText("map.txt", map);
		
		JFrame frame = initiateFrame(width,height); //create frame
		
		Graphics g = frame.getGraphics();
		KeyHandler keyH = new KeyHandler(); // create key handler form class
		frame.addKeyListener(keyH); //add key handler
		
		Camera2D camera = new Camera2D(screenDimensions.multiplyC(0.5),screenDimensions,mapSizePixels); // create camera mid-screen frame dimensions
		Player player = new Player(new Vector2(144,144),tileSize,mapX,mapY,map);//create player at 16,16
		Random rand = new Random();
		
		Enemy[] enemies = new Enemy[150];
		
		for (int i = 0; i < enemies.length; i++) {
		    enemies[i] = new Enemy(
		        new Vector2(128 + i * 64, 128),
		        tileSize,
		        mapX,
		        mapY,
		        player.obstacles
		    );
		}

		
		
		int id;
		
		for(int i = 0 ; i < mapX ; i++) {
			for(int j = 0 ; j < mapY; j++) {
				id = rand.nextInt(50);
				if(id<Food.path.length && map[i][j][1]!=1)
				food[i][j] = new Food(id);
			}
		}
		int hasMoved=0;
		
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		int frames = 0;
		int fps = 0;


		while (true) {
			
			long now = System.nanoTime();
		    lastTime = now;
		    
			hasMoved = player.Update(keyH, food);
		    //enemy updates
			if(hasMoved == 1) {
		    for (Enemy e : enemies) {
		        e.moveTowardsPlayer(player);
		    }
			}
			for (Enemy e : enemies) {
		        e.update(player);
		    }
			
		    camera.render(player, keyH, food, map, enemies);

			//hud.draw((Graphics2D)camera.renderedFrame.getGraphics(), player);
		    g.drawImage(camera.renderedFrame,0,0,null);
		    
		    frames++;

		    // FPS calculation every 1 second
		    if (System.currentTimeMillis() - timer >= 1000) {
		        fps = frames;
		        frames = 0;
		        timer += 1000;

		        System.out.println("FPS: " + fps);
		    }

		}
		
	}
	
	public static JFrame initiateFrame(int width,int height) {
		JFrame frame = new JFrame("Loja2D");
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		return frame;
	}
	public static void loadMapFromText(String resourcePath, int[][][] targetMap) {

	    InputStream is = Main.class
	            .getClassLoader()
	            .getResourceAsStream(resourcePath);

	    if (is == null) {
	        throw new RuntimeException("Resource not found: " + resourcePath);
	    }

	    try (BufferedReader reader =
	                 new BufferedReader(new InputStreamReader(is))) {

	        // Read map size header
	        String[] size = reader.readLine().trim().split(" ");
	        int fileMapX = Integer.parseInt(size[0]);
	        int fileMapY = Integer.parseInt(size[1]);

	        // Safety check
	        if (targetMap.length != fileMapX ||
	            targetMap[0].length != fileMapY ||
	            targetMap[0][0].length < 2) {

	            System.out.println("Map size mismatch!");
	            return;
	        }

	        for (int layer = 0; layer < 2; layer++) {
	            reader.readLine(); // skip "--LAYER x--"

	            for (int y = 0; y < fileMapY; y++) {
	                String[] values = reader.readLine().trim().split("\\s+");

	                for (int x = 0; x < fileMapX; x++) {
	                    targetMap[x][y][layer] = Integer.parseInt(values[x]);
	                }
	            }
	        }

	      //  System.out.println("Map loaded into array successfully!");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}