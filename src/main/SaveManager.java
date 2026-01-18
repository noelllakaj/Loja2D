package main;

import java.io.*;

public class SaveManager {

    // Save inside project folder
    private static final String SAVE_DIR = "./saves";
    private static final String SAVE_PATH = SAVE_DIR + "/save.dat";

    public static void save(Player player) {

        if (!player.isAlive()) {
            System.out.println("Player dead – save skipped");
            return;
        }

        // Ensure folder exists
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        SaveData data = new SaveData();

        // Player info
        data.playerX = (float) player.position.x;
        data.playerY = (float) player.position.y;
        data.playerHealth = player.health;

        // Food[][] -> save positions + ids
        for (int x = 0; x < Food.foodArray.length; x++) {
            for (int y = 0; y < Food.foodArray[x].length; y++) {
                Food f = Food.foodArray[x][y];
                if (f != null) {
                    data.foods.add(new SaveData.FoodEntry(x, y, f.id));
                }
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            out.writeObject(data);
            System.out.println("Game saved at " + SAVE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveExists() {
        return new File(SAVE_PATH).exists();
    }

    public static void load(Player player, int mapX, int mapY) {

        if (!saveExists()) {
            System.out.println("No save file found");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {

            SaveData data = (SaveData) in.readObject();

            // Restore player
            player.position.setEqual(data.playerX, data.playerY);
            player.health = data.playerHealth;

            // Rebuild Food grid
            Food.foodArray = new Food[mapX][mapY];

            for (SaveData.FoodEntry e : data.foods) {
                if (e.x >= 0 && e.y >= 0 && e.x < mapX && e.y < mapY) {
                    Food.foodArray[e.x][e.y] = new Food(e.id);
                }
            }

            System.out.println("Game loaded from " + SAVE_PATH);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
