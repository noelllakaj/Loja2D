package main;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JFrame;

public class Main {
	static int tileSize = 32;
	static int mapX,mapY;
	static int frameWidth = tileSize*24, frameHeight = tileSize*16; //start frame dimensions 32*24=768 32*16=512
	static Vector2 screenDimensions = new Vector2(frameWidth,frameHeight);
	

	public static void main(String[] args) {
		
		int[][][] map;
		map = loadMapFromText("map.txt");
		
		mapX = map.length;
		mapY = map[0].length;
	    Vector2 mapSizePixels = new Vector2(tileSize*mapX,tileSize*mapY);
		
		JFrame frame = initiateFrame(frameWidth,frameHeight); //create frame
		
		KeyHandler keyH = new KeyHandler(); // create key handler form class
		frame.addKeyListener(keyH); //add key handler
		Graphics g = frame.getGraphics();
		
		Camera2D camera = new Camera2D(screenDimensions.multiplyC(0.5),screenDimensions,mapSizePixels); // create camera mid-screen frame dimensions
		Player player = new Player(new Vector2(6,6),tileSize,map);//create player at 144,144
		
		Food.generateFood(map);
		Enemy.spawnEnemies(5, player.obstacles);
		Weapon.generateWeapons(map, Food.foodArray);
		
		
		int targetFPS = 144;
		long optimalTime = 1_000_000_000 / targetFPS;

		//long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		int frames = 0;
		int fps = 0;

		while (true) {
		    long startTime = System.nanoTime();

		    player.Update(keyH, Food.foodArray);
		    Enemy.update(player);
		    camera.render(player, keyH, Food.foodArray, map, Enemy.enemyList,g);
		 

		    frames++;
		    
		    if(keyH.pressedJ) SaveManager.save();
		    if(keyH.pressedK) SaveManager.load();

		    if (System.currentTimeMillis() - timer >= 1000) {
		        fps = frames;
		        frames = 0;
		        timer += 1000;
		        System.out.println("FPS: " + fps);
		    }

		    long elapsedTime = System.nanoTime() - startTime;
		    long sleepTime = optimalTime - elapsedTime;

		    if (sleepTime > 0) {
		        try {
		            Thread.sleep(sleepTime / 1_000_000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
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
	
	
	public static int[][][] loadMapFromText(String resourcePath) {
		int[][][] targetMap = null;
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
	        
	        targetMap = new int[fileMapX][fileMapY][2];
	     
	        for (int layer = 0; layer < 2; layer++) {
	            reader.readLine(); // skip "--LAYER x--"

	            for (int y = 0; y < fileMapY; y++) {
	                String[] values = reader.readLine().trim().split("\\s+");

	                for (int x = 0; x < fileMapX; x++) {
	                    targetMap[x][y][layer] = Integer.parseInt(values[x]);
	                }
	            }
	        }

	       // System.out.println("Map loaded into array successfully!");
	        

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return targetMap;
	}

}