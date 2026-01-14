package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Food {
	String name[] = {"Green Apple","Red Apple","Yellow Apple","Blueberries","Lemon","Cherries","Grape","Pineapple"};
	static String path[] = {"/Food/greenApple.png","/Food/redApple.png","/Food/yellowApple.png",
					 "/Food/blueberries.png","/Food/lemon.png","/Food/cherries.png","/Food/grape.png",
					 "/Food/pineapple.png"};
	
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
	
	public Food(int id) {
		this.id=id;
		try {
			this.sprite = ImageIO.read(getClass().getResourceAsStream(path[id]));
			this.sprite32 = ImageIO.read(getClass().getResourceAsStream(path32[id]));
		} catch (IOException e) {
			System.out.print("negroo");
			e.printStackTrace();
		}
	}
	public String name() {
		return name[this.id];
	}
	public void load () throws IOException {
		this.sprite = ImageIO.read(getClass().getResourceAsStream(path[this.id]));
	}
	
}
