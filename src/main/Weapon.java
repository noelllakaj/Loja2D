package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Weapon {

    public String name;
    public int damage;
    public BufferedImage sprite;
    public Vector2 position = new Vector2();
    private Vector2 targetPosition = new Vector2();
    private double angle = 0;
    public static Weapon[][] weaponArray;
    public int id;

    static String[] names = {
        "Sword", "Axe","Hammer","Mace","Chainsaw","Fire Sword"
        };

    static int[] damages = {1,2,3,4,5,6};

    static String[] paths = {
        "/Weapons/sword.png",
        "/Weapons/axe.png",
        "/Weapons/hammer.png",
        "/Weapons/mace.png",
        "/Weapons/chainsaw.png",
        "/Weapons/fireSword.png"
    };

    public static void generateWeapons(int[][][] map,Food[][] foods) {
    	weaponArray = new Weapon[map.length][map[0].length];
    	int xPos,yPos;
    	Random randomNum = new Random();
    	
    	for(int i = 1 ; i < names.length ; i++) {
    		for(;;) {
    			xPos = randomNum.nextInt(map.length);
    			yPos = randomNum.nextInt(map[0].length);
    			
    			if(map[xPos][yPos][1] == 0 && foods[xPos][yPos] == null && weaponArray[xPos][yPos]==null) {
    				weaponArray[xPos][yPos] = new Weapon(i);
    				break;
    			}
    		}
    	}
    }
    
    public Weapon(int id) {
        this.name = names[id];
        this.damage = damages[id];
        this.id = id;

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
    	switch(player.direction.ordinal()) {
    	case 0 : this.targetPosition.setEqual(Player.position.add(new Vector2(0,offset+offset1*Math.sin(angle)))); break;
    	case 1 : this.targetPosition.setEqual(Player.position.add(new Vector2(offset,offset1*Math.sin(angle)))); break;
    	case 2 : this.targetPosition.setEqual(Player.position.add(new Vector2(0,-offset+offset1*Math.sin(angle)))); break;
    	case 3 : this.targetPosition.setEqual(Player.position.add(new Vector2(-offset,offset1*Math.sin(angle)))); break;
    	}
    }
}