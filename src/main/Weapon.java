package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Weapon {

    public String name;
    public int damage;
    public BufferedImage sprite;
    public Vector2 position = new Vector2();
    private Vector2 targetPosition = new Vector2();
    private double angle = 0;

    static String[] names = {
        "Sword", "Axe"
    };

    static int[] damages = {
        3, // Sword
        2  // Axe
    };

    static String[] paths = {
        "/Weapons/sword.png",
        "/Weapons/axe.png"
    };

    public Weapon(int id) {
        this.name = names[id];
        this.damage = damages[id];

        try {
            sprite = ImageIO.read(
                getClass().getResourceAsStream(paths[id])
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updatePos() {
    	this.position.setEqual(this.targetPosition.sub(position).multiplyC(0.05).add(position));
    }
    
    public void updateTargetPos(Player player) {
    	int offset = 15;
    	int offset1 = 5;
    	angle += 0.01;
    	switch(player.direction) {
    	case 0 : this.targetPosition.setEqual(player.position.add(new Vector2(0,offset+offset1*Math.sin(angle)))); break;
    	case 1 : this.targetPosition.setEqual(player.position.add(new Vector2(offset,offset1*Math.sin(angle)))); break;
    	case 2 : this.targetPosition.setEqual(player.position.add(new Vector2(0,-offset+offset1*Math.sin(angle)))); break;
    	case 3 : this.targetPosition.setEqual(player.position.add(new Vector2(-offset,offset1*Math.sin(angle)))); break;
    	}
    }
}