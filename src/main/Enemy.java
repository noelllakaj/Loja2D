package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {

    Vector2 position = new Vector2();
    Vector2 gridPosition = new Vector2();
    Vector2 targetPosition = new Vector2();

    int tileSize;
    int mapX, mapY;
    boolean[][] obstacles;

    int damage = 1;
    int attackCooldown = 30;
    int attackTimer = 0;

    public Enemy(Vector2 startPos, int tileSize, int mapX, int mapY, boolean[][] obstacles) {
        this.tileSize = tileSize;
        this.mapX = mapX;
        this.mapY = mapY;
        this.obstacles = obstacles;

        this.position.setEqual(startPos);
        this.targetPosition.setEqual(startPos);
        this.gridPosition.setEqual(
            new Vector2((int)startPos.x / tileSize, (int)startPos.y / tileSize)
        );

        walking = true;
        loadAnimations();
    }

    public void update(Player player) {
        if (death) return;

        moveTowardsPlayer(player);
        lerp();
        attack(player);
    }

    private void moveTowardsPlayer(Player player) {

        boolean readyToMove =
            !(this.targetPosition.sub(this.position).magnitude2() > 2);

        if (!readyToMove) return;

        int dx = (int)(player.gridPosition.x - gridPosition.x);
        int dy = (int)(player.gridPosition.y - gridPosition.y);

        // Prioritize axis with bigger distance
        if (Math.abs(dx) > Math.abs(dy)) {
            tryMove(dx > 0 ? 1 : -1, 0);
        } else {
            tryMove(0, dy > 0 ? 1 : -1);
        }
    }

    private void tryMove(int xDir, int yDir) {

        int nx = (int)gridPosition.x + xDir;
        int ny = (int)gridPosition.y + yDir;

        if (nx < 0 || ny < 0 || nx >= mapX || ny >= mapY)
            return;

        if (obstacles[nx][ny]) return;

        gridPosition.x = nx;
        gridPosition.y = ny;

        targetPosition.x = gridPosition.x * tileSize + 15;
        targetPosition.y = gridPosition.y * tileSize + 15;

        updateDirection(xDir, yDir);
    }

    private void updateDirection(int xDir, int yDir) {
        if (xDir == -1) direction = 1;
        if (xDir == 1)  direction = 3;
        if (yDir == -1) direction = 0;
        if (yDir == 1)  direction = 2;
    }

    public void lerp() {
        Vector2 dir = targetPosition.sub(position);
        float dist = (float) dir.magnitude();
        if (dist == 0) return;

        float speed = 0.4f;

        if (dist <= speed) {
            position.setEqual(targetPosition);
            return;
        }

        dir.normalize();
        position.setEqual(position.add(dir.multiplyC(speed)));
    }

    private void attack(Player player) {
        attackTimer++;

        if (attackTimer < attackCooldown) return;

        if (gridPosition.equals(player.gridPosition)) {
            player.health -= damage;
            attackTimer = 0;
        }
    }

    private void loadAnimations() {
        idleAnimation = new BufferedImage[4][2];
        walkingAnimation = new BufferedImage[4][4];

        try {
            idleAnimation[2][0] = load("images");
            idleAnimation[2][1] = load("images");
            // add other directions same way
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage load(String path) throws IOException {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null)
            throw new RuntimeException("Missing resource: " + path);
        return ImageIO.read(stream);
    }
    


}
