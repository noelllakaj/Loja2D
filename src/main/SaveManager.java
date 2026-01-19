package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveManager {
	//line 1 - player grid pos x,y ,health,current weapon id
	//line 2 - player inventory
	//30 lines - weaponArray
	//30 lines - foodArray
	//30 lines - enemyArray
	
	private static String path = "save.txt";
	
	public static void save() {
		try {
            // Project root directory
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, path);

            FileWriter writer = new FileWriter(file);
            writer.write((int)Player.gridPosition.x+" " + " " + (int)Player.gridPosition.x + " ");
            writer.write(Player.health + " " + Player.currentWeapon.id + " ");
            
            writer.write("\n");
            for(Food f : Player.foodInv) {
            	if(f != null) writer.write(f.id + " ");
            	else {
                	writer.write("-1 ");
                	}
            }
            writer.write("\n");
            
            for(int i = 0 ; i < 30 ; i++) {
            	 for(int j = 0 ; j < 30 ; j++) {
                 	if (Weapon.weaponArray[i][j] != null)writer.write(Weapon.weaponArray[i][j].id + " ");
                 	else writer.write("-1 ");
                 }
            	 writer.write("\n");
            }
            
            for(int i = 0 ; i < 30 ; i++) {
           	 for(int j = 0 ; j < 30 ; j++) {
                	if (Food.foodArray[i][j] != null)writer.write(Food.foodArray[i][j].id + " ");
                	else writer.write("-1 ");
                }
           	 writer.write("\n");
           }
           
           for(int i = 0 ; i < Enemy.enemyList.length ; i++) {
                  	if (Enemy.enemyList[i]!= null)writer.write((int)Enemy.enemyList[i].gridPosition.x+ " "
                  			+(int) Enemy.enemyList[i].gridPosition.y+ " " + Enemy.enemyList[i].damage+ " ");
                  	else writer.write("-1 ");
                  
             	 writer.write("\n");
             }
           
            
            
            writer.close();

            System.out.println("Saved to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public static void load() {
		
	
    try {
        File dir = new File("data");
        File file = new File(dir, path);
        if (!file.exists()) {
            System.out.println("No save file found!");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        // ---------- Line 1: Player grid pos, health, current weapon ----------
        String line = reader.readLine();
        String[] parts = line.trim().split("\\s+");
        if (parts.length >= 4) {
            Player.gridPosition.x = Integer.parseInt(parts[0]);
            Player.gridPosition.y = Integer.parseInt(parts[1]);
            Player.position.setEqual(Player.gridPosition); 
            Player.position.setEqual( Player.position.multiplyC(32).add(new Vector2(15,15)));
            Player.targetPosition.setEqual(Player.position);
            Player.health = Integer.parseInt(parts[2]);
            System.out.println(Player.gridPosition.toString() + " " + Player.position.toString()
            +" " + Player.targetPosition.toString());
     
            int weaponId = Integer.parseInt(parts[3]);
            Player.currentWeapon = new Weapon(weaponId); // implement this helper
        }

        // ---------- Line 2: Player inventory ----------
        line = reader.readLine();
        parts = line.trim().split(" ");
        for (int i = 0; i < parts.length && i < Player.foodInv.length; i++) {
            int id = Integer.parseInt(parts[i]);
            if (id != -1) Player.foodInv[i] = new Food(id); // implement helper
            else Player.foodInv[i] = null;
        }

        // ---------- Next 30 lines: Weapon array ----------
        for (int i = 0; i < 30; i++) {
            line = reader.readLine();
            parts = line.trim().split("\\s+");
            for (int j = 0; j < 30 && j < parts.length; j++) {
                int id = Integer.parseInt(parts[j]);
                if (id != -1) Weapon.weaponArray[i][j] = new Weapon(id);
                else Weapon.weaponArray[i][j] = null;
            }
        }

        // ---------- Next 30 lines: Food array ----------
        for (int i = 0; i < 30; i++) {
            line = reader.readLine();
            parts = line.trim().split("\\s+");
            for (int j = 0; j < 30 && j < parts.length; j++) {
                int id = Integer.parseInt(parts[j]);
                if (id != -1) Food.foodArray[i][j] = new Food(id);
                else Food.foodArray[i][j] = null;
            }
        }

        // ---------- Next lines: Enemy list ----------
        for (int i = 0; i < Enemy.enemyList.length; i++) {
            line = reader.readLine();
            if (line == null) break;
            parts = line.trim().split("\\s+");
            if (parts.length >= 3 && !parts[0].equals("-1")) {
                Enemy e = new Enemy(new Vector2((double)Integer.parseInt(parts[0]),(double)Integer.parseInt(parts[1])).multiplyC(32).add(new Vector2(15,15)));
                e.damage = Integer.parseInt(parts[2]);
                Enemy.enemyList[i] = e;
          //      System.out.println(e.toString());
            } else {
                Enemy.enemyList[i] = null;
            }
        }

        reader.close();
        System.out.println("Game loaded successfully!");

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
