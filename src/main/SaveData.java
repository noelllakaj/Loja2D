package main;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {

    private static final long serialVersionUID = 1L;

    // Player
    public float playerX;
    public float playerY;
    public int playerHealth;

    // Food data (tile-based)
    public ArrayList<FoodEntry> foods = new ArrayList<>();

    public static class FoodEntry implements Serializable {
        private static final long serialVersionUID = 1L;

        public int x;
        public int y;
        public int id;

        public FoodEntry(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }
}
