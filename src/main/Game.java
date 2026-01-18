package main;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Game implements Runnable {

    private final boolean loadGame;
    private Thread gameThread;
    private Player player;

    public Game(boolean loadGame) {
        this.loadGame = loadGame;
    }

    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        int[][][] map = Main.loadMapFromText("map.txt");

        Main.mapX = map.length;
        Main.mapY = map[0].length;

        Vector2 mapSizePixels =
                new Vector2(Main.tileSize * Main.mapX,
                            Main.tileSize * Main.mapY);

        JFrame frame = Main.initiateFrame(Main.frameWidth, Main.frameHeight);

        KeyHandler keyH = new KeyHandler();
        frame.addKeyListener(keyH);

        Graphics g = frame.getGraphics();

        Camera2D camera = new Camera2D(
                Main.screenDimensions.multiplyC(0.5),
                Main.screenDimensions,
                mapSizePixels
        );

        player = new Player(new Vector2(144, 144), Main.tileSize, map);

        Food.generateFood(map);
        Enemy.spawnEnemies(10, player.obstacles);
        Weapon.generateWeapons(map, Food.foodArray);

        if (loadGame) {
            SaveManager.load(player,Main.mapX,Main.mapY);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SaveManager.save(player);
            }
        });

        final int targetFPS = 144;
        final long optimalTime = 1_000_000_000 / targetFPS;

        while (true) {
            long start = System.nanoTime();

            if (player.isAlive()) {
                player.Update(keyH, Food.foodArray);
                Enemy.update(player);
            }

            camera.render(player, keyH, Food.foodArray, map, Enemy.enemyList, g);

            long sleep = optimalTime - (System.nanoTime() - start);
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep / 1_000_000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
