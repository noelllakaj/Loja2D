package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Food {
	
	public static Food[][] foodArray;
	
	
	String name[] = {"Green Apple","Red Apple","Yellow Apple","Blueberries","Lemon","Cherries","Grape","Pineapple"};
	static String path[] = {"/Food/greenApple.png","/Food/redApple.png","/Food/yellowApple.png",
					 "/Food/blueberries.png","/Food/lemon.png","/Food/cherries.png","/Food/grape.png",
					 "/Food/pineapple.png"};
	int regenArr[] = {1,1,1,1,1,1,1,1};
	
	static String path32[] = {
		    "/Food/Food32/greenApple.png",
		    "/Food/Food32/redApple.png",
		    "/Food/Food32/yellowApple.png",
		    "/Food/Food32/blueberries.png",
		    "/Food/Food32/lemon.png",
		    "/Food/Food32/cherries.png",
		    "/Food/Food32/grape.png",
		    "/Food/Food32/pineapple.png"
		};
	
	int id;

	BufferedImage sprite;
	BufferedImage sprite32;
	int regen;
	
	public Food(int id) {
		this.id=id;
		try {
			this.sprite = ImageIO.read(getClass().getResourceAsStream(path[id]));
			this.sprite32 = ImageIO.read(getClass().getResourceAsStream(path32[id]));
			this.regen = regenArr[id];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String name() {
		return name[this.id];
	}
	public void load () throws IOException {
		this.sprite = ImageIO.read(getClass().getResourceAsStream(path[this.id]));
	}
	
	public static void generateFood(int[][][] map) {
		
		Random randomId = new Random();
		
		foodArray = new Food[map.length][map[0].length];
		
		int id;
		
		for(int i = 0 ; i < map.length ; i++) {
			for(int j = 0 ; j < map[0].length; j++) {
				id = randomId.nextInt(50);
				if(id<Food.path.length && map[i][j][1]!=1)
				foodArray[i][j] = new Food(id);
			}
		}
	}
	
}
