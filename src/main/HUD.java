package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class HUD {

    private BufferedImage heartFull, heartHalf, heartBlank;

    private int heartSize = 24;    
    private int heartSpacing = 5;  
    
    public HUD() {
        try {
            heartFull  = ImageIO.read(getClass().getResourceAsStream("/hud/heart_full.png"));
            heartHalf  = ImageIO.read(getClass().getResourceAsStream("/hud/heart_half.png"));
            heartBlank = ImageIO.read(getClass().getResourceAsStream("/hud/heart_blank.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, Player player) {

        int x = 40; 
        int y = 447; 

        int heartBackgroundWidth = player.maxHealth * heartSize + (player.maxHealth - 1) * heartSpacing + 10;
        int heartBackgroundHeight = heartSize + 10;

        g2.setColor(Color.YELLOW);
        g2.fillRect(x - 5, y - 5, heartBackgroundWidth, heartBackgroundHeight);
        g2.fillOval(x-21,y-5,34,34);
        g2.fillOval(x-21+heartBackgroundWidth,y-5,34,34);
        g2.setColor(Color.WHITE);
     //   g2.drawRect(x - 5, y - 5, heartBackgroundWidth, heartBackgroundHeight);

        for (int i = 0; i < player.maxHealth; i++) {
            if (player.health >= i + 1) {
                g2.drawImage(heartFull, x + i * (heartSize + heartSpacing), y, heartSize, heartSize, null);
            } else if (player.health > i && player.health < i + 1) {
                g2.drawImage(heartHalf, x + i * (heartSize + heartSpacing), y, heartSize, heartSize, null);
            } else {
                g2.drawImage(heartBlank, x + i * (heartSize + heartSpacing), y, heartSize, heartSize, null);
            }
        }
    }
}
