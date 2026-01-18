package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;

public class Main {

    static int tileSize = 32;
    static int mapX, mapY;

    static int frameWidth = tileSize * 24;
    static int frameHeight = tileSize * 16;
    static Vector2 screenDimensions = new Vector2(frameWidth, frameHeight);

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }

    // Called by menu
    public static void startGame(boolean loadGame) {
        Game game = new Game(loadGame);
        game.start();
    }

    public static JFrame initiateFrame(int width, int height) {
        JFrame frame = new JFrame("Loja2D");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setVisible(true);
        return frame;
    }
    public static int[][][] loadMapFromText(String resourcePath) {

        InputStream is = Main.class
                .getClassLoader()
                .getResourceAsStream(resourcePath);

        if (is == null) {
            throw new RuntimeException("Map file not found: " + resourcePath);
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(is))) {

            // Read map size (width height)
            String[] size = reader.readLine().trim().split("\\s+");
            int mapX = Integer.parseInt(size[0]);
            int mapY = Integer.parseInt(size[1]);

            // [x][y][layer]
            int[][][] map = new int[mapX][mapY][2];

            for (int layer = 0; layer < 2; layer++) {

                reader.readLine(); // skip "--LAYER x--"

                for (int y = 0; y < mapY; y++) {
                    String[] values = reader.readLine().trim().split("\\s+");

                    for (int x = 0; x < mapX; x++) {
                        map[x][y][layer] = Integer.parseInt(values[x]);
                    }
                }
            }

            return map;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load map", e);
        }
    }

}
