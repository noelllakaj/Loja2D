package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Weapon {

    public String name;
    public int damage;
    public BufferedImage sprite;

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



// e shton te playeri ket
Inventory inventory = new Inventory(5);
Weapon equippedWeapon;
int selectedSlot = 0;


//per me ndrruar armet
public void switchWeapon(KeyHandler keyH) {

  //  if (keyH.key1) selectedSlot = 0;
   // if (keyH.key2) selectedSlot = 1;
    ///if (keyH.key3) selectedSlot = 2;
  //if (keyH.key4) selectedSlot = 3;
  //if (keyH.key5) selectedSlot = 4;

    Weapon w = inventory.getWeapon(selectedSlot);
    if (w != null) {
        equippedWeapon = w;
    }
}



//damage
public int getDamage() {
    if (equippedWeapon != null)
        return equippedWeapon.damage;
    return 1; // default damage
	}
}