package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Food {
	String name[] = {"Green Apple","Red Apple","Yellow Apple","Blueberries","Lemon","Cherries","Grape","Pineapple"};
	static String path[] = {"/Food/greenApple.png","/Food/redApple.png","/Food/yellowApple.png",
					 "/Food/blueberries.png","/Food/lemon.png","/Food/cherries.png","/Food/grape.png",
					 "/Food/pineapple.png"};
	int id;

	BufferedImage sprite;
	
	public Food(int id) {
		this.id=id;
		try {
			this.sprite = ImageIO.read(getClass().getResourceAsStream(path[id]));
		} catch (IOException e) {
			System.out.print("negroo");
			e.printStackTrace();
		}
	}
	public String name() {
		return name[this.id];
	}
	public void load () throws IOException {
		this.sprite = ImageIO.read(getClass().getResourceAsStream(this.path[this.id]));
	}
	
}
