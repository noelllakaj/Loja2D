package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
	static int tileSize = 32;
	static int mapX=30,mapY=30;
	static int width = tileSize*24, height = tileSize*16; //start frame dimensions 32*24 32*16
	static Vector2 screenDimensions = new Vector2(width,height);
	static Vector2 mapSizePixels = new Vector2(tileSize*mapX,tileSize*mapY);
	static Food[][] food = new Food[mapX][mapY];

	public static void main(String[] args) {
		
		int[][][] map = new int[mapX][mapY][2];	
		loadMapFromText("map.txt", map);
		BufferedImage[] tiles = new BufferedImage[128];
		loadTiles(tiles);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); //image to use for rendering
		JFrame frame = initiateFrame(width,height); //create frame
		
		Graphics g = frame.getGraphics();
		KeyHandler keyH = new KeyHandler(); // create key handler form class
		frame.addKeyListener(keyH); //add key handler
		
		Camera2D camera = new Camera2D(screenDimensions.multiplyC(0.5),screenDimensions,mapSizePixels); // create camera mid-screen frame dimensions
		Player player = new Player(new Vector2(144,144),tileSize,mapX,mapY);//create player at 16,16
		player.getObstacles(map);
		Random rand = new Random();
		int id;
		for(int i = 0 ; i < mapX ; i++) {
			for(int j = 0 ; j < mapY; j++) {
				id = rand.nextInt(50);
				if(id<Food.path.length && map[i][j][1]!=1)
				food[i][j] = new Food(id);
			}
		}
		
		player.loadAnimations();

		while (true) {
			
		    camera.followPlayer(player);
		    player.move(keyH);
		    player.interact(keyH,food);
		    player.lerp();
		    camera.renderMap(image, tiles, map);
		    camera.renderPlayer(image, player);
		    camera.renderFood(image, food);
		    g.drawImage(image,0,0,null);
		    
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

}